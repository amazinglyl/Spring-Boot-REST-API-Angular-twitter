package rest.twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import rest.twitter.domian.*;
import rest.twitter.repository.*;
import rest.twitter.exception.UserNotFoundException;
import rest.twitter.exception.UserExistException;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Autowired
    UserRepository repository;

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    private final static String KEY_USER="user";

    @GetMapping("/users")
    List<User> all(){
        return repository.findAll();
    }


    @GetMapping("/users/{id}")
    User getUser(@PathVariable long id){
//        try{
//            return repository.getOne(id);
//        }
//        catch (UserNotFoundException e){
//            System.out.println("There is no user"+id);
//
//        }
//        return null;

        // if in cache, get from cache

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



}
