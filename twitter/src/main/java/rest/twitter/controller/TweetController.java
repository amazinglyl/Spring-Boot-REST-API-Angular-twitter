package rest.twitter.controller;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import rest.twitter.domian.*;
import rest.twitter.repository.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TweetController {

    private final static String KEY_TWEET="tweet";
    private static final String KEY_USER_TWEET="keyUserTweet";
    private final static String KEY_TWEET_COMMENTS="tweetcomments";

    //@Resource
    @Autowired
    @Qualifier("template1")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TweetRepository repository;

    @Autowired
    private UserRepository repositoryUser;

    @Autowired
    private CommmetRepository repositoryComment;

    @Autowired
    private LikeTableRepository repositoryLikeTable;

    private Random rand=new Random();

    @Resource(name = "template1")
    private ListOperations<String,CommentTable> listOperations;


    /**
     * common method of redisTemplate
     */
    private boolean hasKey(String key){
        try{
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            return false;
        }
    }

    private boolean expire(String key, long ttl){
        try{
            redisTemplate.expire(key,ttl,TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * get all tweets, no need to cache, just for experiment, namely, unpractical
     */
    @GetMapping("/tweets")
    List<Tweet> getAll() {
        return repository.findAll();
    }

    /**
     * get tweet with tweet_id {id}.
     * Cacheable
     */
    @GetMapping("/tweet/{id}")
    Tweet getOne(@PathVariable long id) {
        // check cache
        HashOperations<String, Long, Tweet> hashOperations = redisTemplate.opsForHash();
        boolean hasKey=hashOperations.hasKey(KEY_TWEET,id);
        if(hasKey) {
            log.info("get from cache");
            return hashOperations.get(KEY_TWEET, id);
        }
        else {
            synchronized(this) {
                hasKey=hashOperations.hasKey(KEY_TWEET,id);
                if(hasKey)
                    return hashOperations.get(KEY_TWEET,id);
                else {
                    Tweet tweet = null;
                    Optional<Tweet> opTweet = repository.findById(id);
                    if (opTweet.isPresent())
                        tweet = opTweet.get();
                    log.info("add to cache");
                    hashOperations.put(KEY_TWEET, id, tweet);
                    long ttl = 100 + rand.nextInt(100);
                    expire(KEY_TWEET, ttl);
                    return tweet;
                }
            }
        }
        //return repository.findById(id).get();
    }

    /**
     * post a tweet
     * cachevict
     */
    @PostMapping("/tweet")
    Tweet createOne(@RequestBody Tweet tweet) {
        // tweet count increase by 1;
        User user = repositoryUser.findById(tweet.getAuthor()).get();
        user.setTweets(user.getTweets() + 1);
        repositoryUser.save(user);

        // delete this tweet's user' all tweets in cache
        String key=KEY_USER_TWEET+tweet.getAuthor();
        if(redisTemplate.hasKey(key))
            redisTemplate.delete(key);
        return  repository.save(tweet);
    }

    /**
     * Get all Comments by the tweet {id}
     * cacheable
     */
    @GetMapping("/tweet/comments/{id}")
    List<CommentTable> getAllComments(@PathVariable long id){

        String key=KEY_TWEET_COMMENTS+id;
        boolean hasKey=hasKey(key);
        if(hasKey){
            log.info("get comments from cache");
            return listOperations.range(key,0,-1);
        }

        List<CommentTable> list = new ArrayList<>(repositoryComment.findByTweet(id));
        log.info("put comments in cache");
        listOperations.leftPushAll(key,list);
        long ttl=100+rand.nextInt(100);
        redisTemplate.expire(key,ttl,TimeUnit.SECONDS);
        return list;
    }

    /**
     * Get all likes of the tweet identified by {id}
     * no need to cache
     */
    @GetMapping("/tweet/likes/{id}")
    List<LikeTable> getAllLikes(@PathVariable long id){
        return new ArrayList<>(repositoryLikeTable.findByTweet(id));
    }

    /**
     * create a new comment
     *cacheevict
     */
    @PostMapping("/tweet/comment")
    CommentTable createComment(@RequestBody CommentTable table){
        //Increase the comments count of corresponding tweet
        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setComments(tweet.getComments()+1);
        repository.save(tweet);

        //delete corresponding tweet's comments list in cache if exist.
        String key = KEY_TWEET_COMMENTS+table.getTweet();
        if(redisTemplate.hasKey(key))
            redisTemplate.delete(key);

        //put this new comment object in CommentTable
        return repositoryComment.save(table);
    }

    /**
     * post a new like
     */
    @PostMapping("/tweet/like")
    LikeTable createLike(@RequestBody LikeTable table){

        //Increase the likes count of corresponding tweet
        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setLikes(tweet.getLikes()+1);
        repository.save(tweet);

        //put a new comment object in CommentTable
        return repositoryLikeTable.save(table);
    }

    /**
     *   delete comment with {id}
     *   cacheevict tweet's list in cache if exists, and this comment in cache if exist.
     */
    @DeleteMapping("tweet/comment/{id}")
    void deleteComment(@PathVariable long id){
        CommentTable table = repositoryComment.findById(id).get();
        String key=KEY_TWEET_COMMENTS+table.getTweet();
        if(redisTemplate.hasKey(key))
            listOperations.remove(KEY_TWEET_COMMENTS+table.getTweet(),1,table);

        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setComments(tweet.getComments()-1);
        repository.save(tweet);

        repositoryComment.deleteById(id);
    }

    /**
     *  unclick like
     */
    @DeleteMapping("tweet/like/{id}")
    void deleteLike(@PathVariable long id){
        LikeTable table = repositoryLikeTable.findById(id).get();
        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setComments(tweet.getComments()-1);
        repository.save(tweet);

        repositoryComment.deleteById(id);
    }

    /**
     * delete tweet has {id}
     * cacheevict
     * @param id
     * @return
     */
    @DeleteMapping("tweet/{id}")
    String deleteTweet(@PathVariable long id){
        Tweet tweet = repository.findById(id).get();

        //delete this tweet in cache if exists
        HashOperations<String, Long, Tweet> hashOperations = redisTemplate.opsForHash();
        if(hashOperations.hasKey(KEY_TWEET,id))
            hashOperations.delete(KEY_TWEET,id);

        // delete corresponding comments list if exist in cache if exists
        if(hasKey(KEY_TWEET_COMMENTS+id))
            redisTemplate.delete(KEY_TWEET_COMMENTS+id);

        // delete this tweet's author's tweets list in cache if exists
        if(hasKey(KEY_USER_TWEET+tweet.getAuthor()))
            redisTemplate.delete(KEY_USER_TWEET+tweet.getAuthor());

        // delete data in database
        tweet.setDisable(true);
        repository.save(tweet);
        return "Delete Successfully";
    }
}
