package com.example.photoappusers.services;

import com.example.photoappusers.Domain.UserDto;
import com.example.photoappusers.RestModel.AlbumResponseModel;
import com.example.photoappusers.data.AlbumsServiceClient;
import com.example.photoappusers.data.UserEntity;
import com.example.photoappusers.repository.UsersRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements  UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private UsersRepository usersRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
//    RestTemplate restTemplate;
    AlbumsServiceClient albumsServiceClient;
    Environment env;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           AlbumsServiceClient albumsServiceClient,
                           Environment env) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.albumsServiceClient = albumsServiceClient;
        this.env = env;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        log.info("Start createUser: " + userDetails.toString());

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        log.info("Saving userEntity: [" + userEntity.toString() + "]");

        UserEntity responseEntity = usersRepository.save(userEntity);
        log.info("Saved responseEntity: [" + responseEntity .toString()+ "]");
        log.info("end createUser:");
        return modelMapper.map(responseEntity, UserDto.class);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);



        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        log.info("Start GetUser: " + userId);

        UserEntity userEntity = usersRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UsernameNotFoundException("User not found with id = " + userId);
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);

/*
        String albumsUrl = String.format(env.getProperty("albums.url"), userId);
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
        */

        if(albumsServiceClient != null) {
            log.info("Before calling albums Microservice");
            List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
            userDto.setAlbums(albumsList);
            log.info("After calling albums Microservice");
        } else {
            log.info("AlbumsServiceClient is null");


        }

        log.info("Completed GetUser: " + userDto.toString());

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(userName);
        if(userEntity == null) {
            throw new UsernameNotFoundException(userName);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }
}
