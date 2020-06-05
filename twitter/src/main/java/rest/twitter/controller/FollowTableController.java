package rest.twitter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import rest.twitter.domian.*;
import rest.twitter.repository.*;
import rest.twitter.exception.*;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class FollowTableController {

    @Autowired
    FollowTableRepository repository;

    @Autowired
    UserRepository repositoryUser;

    @Autowired
    TweetRepository repositoryTweet;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    ListOperations<String, Tweet> listOperations;

    @Resource(name = "redisTemplate")
    ListOperations<String, User> listOperationsUser;

    private static final String KEY_USER_TWEET="keyUserTweet";
    private static final String KEY_USER_FOLLOW="followers";

    /**
     * get all follow pairs
     * not practical
     */
    @GetMapping("/follows")
    List<FollowTable> getAll(){
        return repository.findAll();
    }

    /**
     * check if already follows
     * @param table
     * @return
     */
    @GetMapping("/follow/check")
    boolean check(@RequestBody FollowTable table){
        //return repository.findById(new FollowTableId(table.getFollowedId(),table.getFollowerId())).isPresent();
        return repository.countByFollowedIdAndFollowerId(table.getFollowedId(),table.getFollowerId())!=0;
    }

    /**
     * second way to check if already follows
     * @param followedId
     * @param followerId
     * @return
     */
    @GetMapping("/follow/check2")
    boolean check(@RequestParam(value="followedId") long followedId,@RequestParam(value="followerId") long followerId){
        //return repository.findById(new FollowTableId(table.getFollowedId(),table.getFollowerId())).isPresent();
        return repository.countByFollowedIdAndFollowerId(followedId,followerId)!=0;
    }

    /**
     * get all followers' Id of User {id}
     * @param id
     * @return
     */
    @GetMapping("/follow/{id}")
    List<FollowTable> getFollowersId(@PathVariable long id){
        return repository.findByFollowedId(id);
    }

    /**
     * get all followers' User domain with User's {id}
     */
    @GetMapping("/follow/follower")
    List<User> getFollowers(@RequestParam(value="id",required = true) long id){

        return repository.findByFollowedId(id).stream()
                .map(x->repositoryUser.findById(x.getFollowerId())
                        .orElseThrow(()->new UserNotFoundException(x.getFollowerId()))).collect(Collectors.toList());
//        log.info("getFollowTable");
//        List<FollowTable> list=  repository.findByFollowedId(id);
//        log.info("geUser");
//        List<User>  res= new ArrayList<>();
//
//        for (FollowTable f : list) {
//            long followerId=f.getFollowerId();
//            User user=repositoryUser.findById(followerId).orElseThrow(()->new UserNotFoundException(followerId));
//            res.add(user);
//        }
//        return res;
    }

    /**
     * get all following User domain for User {id}
     * @param id
     * @return
     */
    @GetMapping("follow/following")
    List<User> getFollowings(@RequestParam(value="id",required = true) long id) {
        String key = KEY_USER_FOLLOW+id;

        // get if in cache
        if(redisTemplate.hasKey(key))
            return listOperationsUser.range(key,0,-1);

        //get from database and save in cache
        List<User> list= repository.findByFollowerId(id).stream()
                .map(x -> repositoryUser.findById(x.getFollowedId())
                        .orElseThrow(() -> new UserNotFoundException(x.getFollowedId()))).collect(Collectors.toList());
        listOperationsUser.leftPushAll(key,list);
        return list;
    }

    /**
     * follow other people
     */
    @PostMapping("/follow")
    FollowTable postFollow(@RequestBody FollowTable table){

        //User with followedId in table increase follower number by 1;
        User user = repositoryUser.findById(table.getFollowedId()).get();//.orElseThrow(()->new UserNotFoundException(table.getFollowedId()));
        user.setFollowers(user.getFollowers()+1);
        repositoryUser.save(user);

        //User with followerId in table increase following number by 1;
        User user2 = repositoryUser.findById(table.getFollowerId()).get();//.orElseThrow(()->new UserNotFoundException(table.getFollowedId()));
        user2.setFollowings(user.getFollowings()+1);
        repositoryUser.save(user2);

        //delete cache
        String key=KEY_USER_FOLLOW+table.getFollowerId();
        if(redisTemplate.hasKey(key))
            redisTemplate.delete(key);
        if(redisTemplate.hasKey(KEY_USER_TWEET+table.getFollowerId()))
            redisTemplate.delete(KEY_USER_TWEET+table.getFollowerId());
        //put table;
        return repository.save(table);
    }

    /**
     * delete follow
     */
    @DeleteMapping("follow")
    void deleteFollow(@RequestBody FollowTable table){

        //User with followedId in table decrease follower number by 1;
        User user = repositoryUser.findById(table.getFollowedId()).get();//.orElseThrow(()->new UserNotFoundException(table.getFollowedId()));
        user.setFollowers(user.getFollowers()-1);
        repositoryUser.save(user);

        //User with followerId in table decrease following number by 1;
        User user1 = repositoryUser.findById(table.getFollowerId()).get();//.orElseThrow(()->new UserNotFoundException(table.getFollowedId()));
        user1.setFollowings(user.getFollowings()-1);
        repositoryUser.save(user1);

        //delete cache
        String key=KEY_USER_FOLLOW+table.getFollowedId();
        if(redisTemplate.hasKey(key))
            redisTemplate.delete(key);
        if(redisTemplate.hasKey(KEY_USER_TWEET+table.getFollowerId()))
            redisTemplate.delete(KEY_USER_TWEET+table.getFollowerId());
        //delete this followMap
        repository.delete(table);
    }


    // get all active tweets for User {id}

    @GetMapping("/usertweets/{id}")
    List<Tweet> getSelfTweet(@PathVariable long id) {

        String key=KEY_USER_TWEET+id;
        log.info(key);
//        if(redisTemplate.hasKey(key)) {
//            log.info("inside");
//            return listOperations.range(key, 0, -1);
//        }
        log.info("good");
        List<Tweet> res = repositoryTweet.findByAuthorAndDisable(id,false);
        log.info(""+res.size());
        listOperations.leftPushAll(key,res);
        return res;
    }

    // get all active tweets of followed User of User {id}

    @GetMapping("/useralltweets/{id}")
    List<Tweet> getAllTweet(@PathVariable long id) {
        List<FollowTable> list = repository.findByFollowerId(id);
        List<Tweet> res = new ArrayList<>();
        for(FollowTable f:list){

            String key=KEY_USER_TWEET+f.getFollowedId();
            if(false&&redisTemplate.hasKey(key)) {
                log.info("get all tweets from cache");
                res.addAll(listOperations.range(key, 0, -1));
            }

            else {
                log.info("get all tweets from db");
                List<Tweet> tweetList = repositoryTweet.findByAuthorAndDisable(f.getFollowedId(), false);
                listOperations.leftPushAll(key, tweetList);
                res.addAll(tweetList);
            }

        }
        return res;
    }
}
