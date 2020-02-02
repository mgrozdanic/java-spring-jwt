package com.example.demo.services.interfaces;

import com.example.demo.models.User;

public interface UsersServiceInterface {
    User save(User user);
    boolean emailIsUnique(String email);
    User loggedUser(String email);
}
