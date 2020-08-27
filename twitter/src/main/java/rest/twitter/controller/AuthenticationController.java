package rest.twitter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rest.twitter.domian.Dto;
import rest.twitter.domian.User;
import rest.twitter.repository.UserRepository;
import rest.twitter.security.JwtTokenProvidor;
import rest.twitter.security.JwtTokenUserDetailsService;

import java.util.List;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvidor jwtTokenProvidor;
    @Autowired
    private JwtTokenUserDetailsService jwtTokenUserDetailsService;
    @Autowired
    private UserRepository repository;

    @PostMapping("/authenticate")
    public Dto authenticate(@RequestParam String name, @RequestParam String password){

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(name,password);
        Authentication authentication=authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<User> user=repository.findByName(name);
        Dto dto=new Dto(user.get(0).getId(),jwtTokenProvidor.generateToken(name));
        return dto;
    }


}
