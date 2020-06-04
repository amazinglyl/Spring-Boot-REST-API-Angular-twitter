package rest.twitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rest.twitter.domian.FollowTable;
import rest.twitter.domian.LikeTable;
import rest.twitter.domian.Tweet;
import rest.twitter.domian.User;
import rest.twitter.repository.FollowTableRepository;
import rest.twitter.repository.LikeTableRepository;
import rest.twitter.repository.TweetRepository;
import rest.twitter.repository.UserRepository;

import javax.annotation.Resource;

@Component
@Slf4j
public class CronJob {

    @Autowired
    TweetRepository repository;
    @Autowired
    LikeTableRepository likeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowTableRepository followTableRepository;
    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Resource(name="redisTemplate")
    ListOperations<String,Tweet> listOperations;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * put hot tweets in cache
     */
    @Scheduled(fixedRate = 200000)
    public void searchHotTweets() {
        Sort sort = Sort.by("visit").descending();
        List<Tweet> list =repository.findFirst50ByDisable(false,sort);
        String key="hotTweets";
        listOperations.leftPushAll(key,list);
        redisTemplate.expire(key,30, TimeUnit.SECONDS);
    }

    /**
     * which to read
     * 线程池和hashtable去handle
     */
    @Scheduled(fixedRate = 200000)
    public void tweetRecommand(){

        List<LikeTable> list = likeRepository.findAll();
        Map<Long,List<Long>> map = new HashMap<>();
        for(LikeTable lt:list){
            long author = lt.getAuthor();
            if(!map.containsKey(author))
                map.put(author,new ArrayList<Long>());
            map.get(author).add(lt.getTweet());
        }

        ThreadPoolExecutor threadpool = new ThreadPoolExecutor(10,15,60,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        long usersNum = userRepository.count();
        List<User> users = userRepository.findAll();
        for(User user: users){
            log.info("new pool run");
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    List<Long> followings = followTableRepository.findByFollowerId(user.getId()).stream().map(x->x.getFollowedId()).collect(Collectors.toList());
                    Set<Long> recommandTweetsId = new HashSet<>();
                    int cap=100;
                    for(long following:followings){
                        if(map.containsKey(following))
                            recommandTweetsId.addAll(map.get(following));
                        if(recommandTweetsId.size()>=cap){
                            break;
                        }
                    }
                    if(recommandTweetsId.size()>0)
                        redisTemplate.opsForValue().set("recommandTweets"+user.getId(),recommandTweetsId);
                }
            });
        }
    }

    /**
     * which to read;
     */
}