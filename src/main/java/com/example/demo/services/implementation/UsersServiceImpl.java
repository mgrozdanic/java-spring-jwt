package com.example.demo.services.implementation;

import com.example.demo.models.User;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.interfaces.UsersServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsersServiceImpl implements UsersServiceInterface, UserDetailsService {

    @Autowired
    private UsersRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public boolean emailIsUnique(String email){
        return repository.emailUnique(email) == 0;
    }

    @Override
    public User loggedUser(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repository.findByEmail(email);
        if (user != null){
            //do stuff, return
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                    new ArrayList<>());
        }

        throw new UsernameNotFoundException("User not found with username: " + email);
    }
}
