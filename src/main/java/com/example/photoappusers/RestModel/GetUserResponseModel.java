package com.example.photoappusers.RestModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponseModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AlbumResponseModel> albums;

}
