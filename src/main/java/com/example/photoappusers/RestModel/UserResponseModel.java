package com.example.photoappusers.RestModel;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserResponseModel {
    private String firstName;

    private String lastName;

    private String email;

    private String userId;
}
