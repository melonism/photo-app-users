package com.example.photoappusers.RestModel;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequestModel {
    @NotNull(message="Missing First Name")
    @Size(min=2,message="First Name must be greater than 2 characters")
    private String firstName;

    @NotNull(message="Missing Last Name")
    @Size(min=2,message="Last Name must be greater than 2 characters")
    private String lastName;

    @NotNull(message="Missing email")
    @Email
    private String email;

    @NotNull(message="Missing password")
    @Size(min=8,max=16,message="Password must be greater than 8 chars and less than 16")
    private String password;
}
