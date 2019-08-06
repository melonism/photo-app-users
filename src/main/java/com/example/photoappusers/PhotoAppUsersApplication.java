package com.example.photoappusers;

import com.example.photoappusers.Domain.FeignErrorDecoder;
import com.example.photoappusers.services.UserServiceImpl;
import feign.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
//@EnableFeignClients(basePackages = {"com.example.photoappusers"})
public class PhotoAppUsersApplication {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PhotoAppUsersApplication.class);

    public static void main(String[] args) {
        log.info("Starting PhotoAppUsersApplication");
        SpringApplication.run(PhotoAppUsersApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignErrorDecoder getFeignErrorDecoder() {
        return new FeignErrorDecoder();
    }

}
