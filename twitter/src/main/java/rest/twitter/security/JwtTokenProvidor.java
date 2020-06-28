package rest.twitter.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import rest.twitter.exception.TokenInvalidException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvidor {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expire}")
    private long expire;

    @Autowired
    JwtTokenUserDetailsService jwtTokenUserDetailsService;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateToken(String name){
        Claims claims= Jwts.claims().setSubject(name);
        Date currentTime= new Date();
        Date validity=new Date(currentTime.getTime()+expire);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String resolveToken(HttpServletRequest request){
        String token=request.getHeader("Authorization");
        if(token!=null&&token.startsWith("Bearer ")){
            return token.substring(7);
        }
        else
            return null;
    }

    public boolean isValidToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }
        catch (Exception ex){
            throw new TokenInvalidException();
        }
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = jwtTokenUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token){
        String name=Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        return name;
    }
}
