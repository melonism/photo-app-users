package com.example.photoappusers.controller;

import com.example.photoappusers.Domain.UserDto;
import com.example.photoappusers.RestModel.GetUserResponseModel;
import com.example.photoappusers.RestModel.UserRequestModel;
import com.example.photoappusers.RestModel.UserResponseModel;
import com.example.photoappusers.services.UserService;
import com.example.photoappusers.services.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersControllers {
    private static final Logger log = LoggerFactory.getLogger(UsersControllers.class);

    @Autowired
    private Environment env;

    @Autowired
    UserService userService;

    @GetMapping("/status/check")
    public String status() {
        return "Working" + env.getProperty("local.server.port") + ", with token = " + env.getProperty("token.secret");
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody UserRequestModel userDetails) {
        log.info("Start createUsers: " + userDetails.toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto responseUserDto = userService.createUser(userDto);

        UserResponseModel returnValue = modelMapper.map(responseUserDto, UserResponseModel.class);

        log.info("Completed createUsers: " + returnValue.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping(value="/{userId}",
            produces = {
                        MediaType.APPLICATION_XML_VALUE,
                        MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<GetUserResponseModel> getUser(@PathVariable("userId") String userId) {
        log.info("Start getUsers: " + userId);

        UserDto userDto = userService.getUserByUserId(userId);
        GetUserResponseModel returnValue = new ModelMapper().map(userDto, GetUserResponseModel.class);
        log.info("Completed getUsers");

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
