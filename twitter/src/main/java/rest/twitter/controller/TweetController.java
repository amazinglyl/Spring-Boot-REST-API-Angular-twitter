package rest.twitter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import rest.twitter.domian.*;
import rest.twitter.repository.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TweetController {

//    @Autowired
//    RedisTemplate redisTemplate;

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


    //HashOperations<String, Long, Tweet> hashOperations = redisTemplate.opsForHash();

//    @Resource(name = "redisTemplate")
//    HashOperations<String, Long, Tweet> hashOperations;

    private final static String KEY_TWEET="tweet";


    @GetMapping("/tweets")
    List<Tweet> getAll() {
        return repository.findAll();
    }

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
        long ttl=10;
        redisTemplate.expire(KEY_TWEET,ttl, TimeUnit.SECONDS);
        return tweet;
        //return repository.findById(id).get();
    }

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
        return new ArrayList<>(repositoryComment.findByTweet(id));
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
        Tweet tweet = repository.findById(id).get();
        tweet.setDisable(true);
        repository.save(tweet);
        return "Delete Successfully";
    }
}
