package com.example.demo.DTOs;

public class UserDataDto {
    public String token;
    public String userName;
    public String firstName;
    public String lastName;
    public String email;

    public UserDataDto(String token, String userName, String firstName, String lastName, String email) {
        this.token = token;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
