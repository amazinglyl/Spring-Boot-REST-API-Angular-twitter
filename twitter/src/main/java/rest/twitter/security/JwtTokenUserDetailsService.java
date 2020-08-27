package rest.twitter.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rest.twitter.domian.User;
import rest.twitter.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class JwtTokenUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> userList=userRepository.findByName(username);
        if(userList.size()==0){
            log.info("User not Found");
            throw new UsernameNotFoundException("User Not Found");
        }

        User user=userList.get(0);
        String passwordEncode=passwordEncoder.encode(user.getPassword());
        GrantedAuthority authority=new SimpleGrantedAuthority("USER");
        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password(passwordEncode)//
                .authorities(authority)
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();

    }
}
