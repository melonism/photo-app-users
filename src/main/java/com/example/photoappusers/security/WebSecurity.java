package com.example.photoappusers.security;

import com.example.photoappusers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configurable
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private Environment env;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(Environment env,
                       UserService userService,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        String gatewayIp = env.getProperty("gateway.ip");
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/**").hasIpAddress(gatewayIp);
        http.authorizeRequests().antMatchers("/users/**").permitAll()
        .and().addFilter(getAuthenticationFilter());
        http.headers().frameOptions().disable();

    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, env, authenticationManager());
//        authenticationFilter.setAuthenticationManager(authenticationManager());
        authenticationFilter.setFilterProcessesUrl(env.getProperty("login.url.path"));
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
