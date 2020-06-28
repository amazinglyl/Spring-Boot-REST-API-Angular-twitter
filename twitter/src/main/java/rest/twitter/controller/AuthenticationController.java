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
import rest.twitter.security.JwtTokenProvidor;
import rest.twitter.security.JwtTokenUserDetailsService;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvidor jwtTokenProvidor;

    @Autowired
    private JwtTokenUserDetailsService jwtTokenUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String name, @RequestParam String password){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(name,password);
        String password1=jwtTokenUserDetailsService.loadUserByUsername(name).getPassword();
        String password2=authenticationToken.getCredentials().toString();

        log.info(""+passwordEncoder.matches(password2,password1));

        log.info("I am here"+authenticationToken.getCredentials().toString());
        Authentication authentication=authenticationManager.authenticate(authenticationToken);
        log.info("I am here 2");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvidor.generateToken(name);
    }
}
