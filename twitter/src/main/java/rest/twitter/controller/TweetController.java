package rest.twitter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final static String KEY_TWEET_COMMENTS="tweetcomments";

//    @Autowired
//    RedisTemplate redisTemplate;

    //@Resource
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    TweetRepository repository;

    @Autowired
    UserRepository repositoryUser;

    @Autowired
    CommmetRepository repositoryComment;

    @Autowired
    LikeTableRepository repositoryLikeTable;

    private Random rand=new Random();

//    @Resource(name = "redisTemplate")
//    HashOperations<String, Long, Tweet> hashOperations;

    @Resource(name = "redisTemplate")
    ListOperations<String,CommentTable> listOperations;


    // common method of redisTemplate
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


    // get all tweets
    @GetMapping("/tweets")
    List<Tweet> getAll() {
        return repository.findAll();
    }

    // get tweet with {id}
    @GetMapping("/tweet/{id}")
    Tweet getOne(@PathVariable long id) {

        HashOperations<String, Long, Tweet> hashOperations = redisTemplate.opsForHash();
        boolean hasKey=hashOperations.hasKey(KEY_TWEET,id);
        if(hasKey) {
            log.info("get from cache");
            return hashOperations.get(KEY_TWEET, id);
        }

        //not in cache
        Tweet tweet=null;
        Optional<Tweet> opTweet=repository.findById(id);
        if(opTweet.isPresent())
            tweet=opTweet.get();
        log.info("add to cache");
        hashOperations.put(KEY_TWEET,id,tweet);
        long ttl=100+rand.nextInt(100);
        expire(KEY_TWEET,ttl);
        return tweet;
        //return repository.findById(id).get();
    }

    //post a tweet
    @PostMapping("/tweet")
    Tweet createOne(@RequestBody Tweet tweet) {

        // tweet count increase by 1;
        User user = repositoryUser.findById(tweet.getAuthor()).get();
        user.setTweets(user.getTweets() + 1);
        repositoryUser.save(user);

        return  repository.save(tweet);

        // create User Tweet relation in UserTweetMap table
        //repositoryUserTweetMap.save(new UserTweetsMap(tweet.getAuthor(), newTweet.getId()));

        //Create a new tweet in Tweet Table
    }

    //Get all Comments by the tweet {id}

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

    //Get all likes of the tweet identified by {id}

    @GetMapping("/tweet/likes/{id}")
    List<LikeTable> getAllLikes(@PathVariable long id){
        return new ArrayList<>(repositoryLikeTable.findByTweet(id));
    }


    // create a new comment

    @PostMapping("/tweet/comment")
    CommentTable createComment(@RequestBody CommentTable table){

        //Increase the comments count of corresponding tweet

        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setComments(tweet.getComments()+1);
        repository.save(tweet);

        //put a new comment object in CommentTable

        return repositoryComment.save(table);

        //put a new relation in TweetCommentsMap

        //repositoryTweetCommentsMap.save(new TweetCommentsMap(table.getTweet(),comment.getId()));



    }

    //post new like

    @PostMapping("/tweet/like")
    LikeTable createLike(@RequestBody LikeTable table){

        //Increase the likes count of corresponding tweet

        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setLikes(tweet.getLikes()+1);
        repository.save(tweet);

        //put a new comment object in CommentTable

        return repositoryLikeTable.save(table);

        //put a new relation in TweetCommentsMap

        //repositoryTweetLikesMap.save(new TweetLikesMap(table.getTweet(),newLike.getId()));


    }

    //delete comment with {id}

    @DeleteMapping("tweet/comment/{id}")
    void deleteComment(@PathVariable long id){

        CommentTable table = repositoryComment.findById(id).get();

        listOperations.remove(KEY_TWEET_COMMENTS+table.getTweet(),1,table);

        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setComments(tweet.getComments()-1);
        repository.save(tweet);

        repositoryComment.deleteById(id);

        //repositoryTweetCommentsMap.delete(repositoryTweetCommentsMap.findByCommentId(id));
    }

    //delete like with {id}

    @DeleteMapping("tweet/like/{id}")
    void deleteLike(@PathVariable long id){

        LikeTable table = repositoryLikeTable.findById(id).get();

        Tweet tweet = repository.findById(table.getTweet()).get();
        tweet.setComments(tweet.getComments()-1);
        repository.save(tweet);

        repositoryComment.deleteById(id);

        //repositoryTweetLikesMap.delete(repositoryTweetLikesMap.findByLikeId(id));
    }

    @DeleteMapping("tweet/{id}")
    String deleteTweet(@PathVariable long id){

        //delete this tweet in cache
        HashOperations<String, Long, Tweet> hashOperations = redisTemplate.opsForHash();
        if(hashOperations.hasKey(KEY_TWEET,id))
            hashOperations.delete(KEY_TWEET,id);

        // delete corresponding comments list if exist in cache
        if(hasKey(KEY_TWEET_COMMENTS+id))
            redisTemplate.delete(KEY_TWEET_COMMENTS+id);

        // delete data in database
        Tweet tweet = repository.findById(id).get();
        tweet.setDisable(true);
        repository.save(tweet);
        return "Delete Successfully";
    }
}
