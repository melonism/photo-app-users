package com.example.photoappusers.security;

import com.example.photoappusers.Domain.UserDto;
import com.example.photoappusers.RestModel.LoginRequestModel;
import com.example.photoappusers.services.UserService;
import com.example.photoappusers.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UserService userService;
    private Environment env;


    public AuthenticationFilter ( UserService userService,
                                  Environment env,
                                  AuthenticationManager authenticationManager) {
       this.userService = userService;
       this.env = env;
       super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
        throws AuthenticationException{

        log.info("Start Login: attemptAuthentication");
        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginRequestModel.class);

            log.info("Processing Login: " + creds.toString());
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            log.info("Failed Login: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth)
        throws IOException, ServletException {

        log.info("Start Login: successfulAuthentication");

        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDetailsByEmail(userName);

        // Generate JWT token
        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret") )
                .compact();

        res.addHeader("token", token);
        res.addHeader("userId",userDto.getUserId());

        log.info("Completed Login: ");
        log.info("Token: " + token);
        log.info("UserId: " + userDto.getUserId());
        log.info("ExpirationTime: " + env.getProperty("token.expiration_time"));
        log.info("ExpirationSecret: " + env.getProperty("token.secret"));

    }
}
