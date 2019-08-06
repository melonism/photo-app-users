package com.example.photoappusers.Domain;

import com.example.photoappusers.RestModel.AlbumResponseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = -8851745509113017905L;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;
    private String encryptedPassword;
    private List<AlbumResponseModel> albums;
}
