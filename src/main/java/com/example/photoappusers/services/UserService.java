package com.example.photoappusers.services;

import com.example.photoappusers.Domain.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDetails);
    UserDto getUserDetailsByEmail(String email);
    UserDto getUserByUserId(String userId);
}
