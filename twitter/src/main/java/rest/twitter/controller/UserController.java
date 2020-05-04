package rest.twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rest.twitter.domian.*;
import rest.twitter.repository.*;
import rest.twitter.exception.UserNotFoundException;
import rest.twitter.exception.UserExistException;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository repository;

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

          return repository.findById(id).orElseThrow(()->new UserNotFoundException(id));
    }

    @PostMapping("/users")
    User createUser(@RequestBody User user){
        List<User> list=repository.findByName(user.getName());
        if(list.size()!=0)
            throw new UserExistException();
        return repository.save(user);
    }



}
