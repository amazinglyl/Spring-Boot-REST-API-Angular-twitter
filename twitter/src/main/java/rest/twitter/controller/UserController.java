package rest.twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import rest.twitter.domian.*;
import rest.twitter.repository.*;
import rest.twitter.exception.UserNotFoundException;
import rest.twitter.exception.UserExistException;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserRepository repository;

    @Autowired
    TweetRepository tweetRepository;

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "redisTemplate")
    HashOperations<String,String,Integer> hashOperations;

    @Resource(name = "redisTemplate")
    HashOperations<String,String,String> hashOperations2;

    private final static String KEY_USER="user";

    /**
     * get all users
     * @return
     */
    @GetMapping("/users")
    List<User> all(){
        return repository.findAll();
    }

    /**
     * get for login
     * @param name
     * @param password
     * @return
     */
    @GetMapping("/user")
    User login(@RequestParam String name, @RequestParam String password){
        List<User> user = repository.findByName(name);
        return user.get(0);
    }

    /**
     * get user information based on id
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    User getUser(@PathVariable long id){

        HashOperations<String, Long, User> hashOperations=redisTemplate.opsForHash();
        if(hashOperations.hasKey(KEY_USER,id)){
            return hashOperations.get(KEY_USER,id);
        }

        //check database and save in cache

        User user = repository.findById(id).orElseThrow(()->new UserNotFoundException(id));
        hashOperations.put(KEY_USER,id,user);
        long ttl=100;
        redisTemplate.expire(KEY_USER,ttl, TimeUnit.SECONDS);

        return user;
    }

    @PostMapping("/users")
    User createUser(@RequestBody User user){
        List<User> list=repository.findByName(user.getName());
        if(list.size()!=0)
            throw new UserExistException();
        return repository.save(user);
    }

    /**
     * get current hot tweets from cahce
     * @return
     */
    @GetMapping("/hotTweets")
    List<Tweet> getHotTweets(){
//        Sort sort = Sort.by("visit").descending();
//        List<Tweet> list =tweetRepository.findFirst50ByDisable(false,sort);
//        return list;
        return redisTemplate.opsForList().range("hotTweets",0,-1).stream().map(x->(Tweet)x).collect(Collectors.toList());
    }

    /**
     * get recommand tweets for user {id}
     */
    @GetMapping("/recommandTweets/{id}")
    List<Tweet> getRecommandTweets(@PathVariable long id){
        Set<Long> set = new HashSet<>();
        List<Tweet> list = new ArrayList<>();
        String key = "recommandTweets"+id;
        if(!redisTemplate.hasKey(key))
            return list;
        set = (Set<Long>) redisTemplate.opsForValue().get("recommandTweets"+id);
        for(long tweetId:set){
            list.add(tweetRepository.findById(tweetId).get());
        }
        return list;
    }

    @GetMapping("hotWords")
    Map<String, String> getHotWords(){
        String key="hotWords";
        return hashOperations2.entries(key);
    }

    @GetMapping("test")
    String test(){
        String key="foo";
        return (String)redisTemplate.opsForValue().get(key);
        }

}
