package com.example.demo.controllers;

import com.example.demo.DTOs.LoginDataDto;
import com.example.demo.DTOs.RegisterDataDto;
import com.example.demo.DTOs.UserDataDto;
import com.example.demo.models.User;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.services.implementation.UsersServiceImpl;
import com.example.demo.services.interfaces.UsersServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping
@RestController
public class UsersController {

    @Autowired
    private UsersServiceInterface usersService;

    @Autowired
    private UsersServiceImpl usersServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody RegisterDataDto registerData) throws Exception {
        User user = new User(registerData.userName, passwordEncoder.encode(registerData.password),
                registerData.firstName, registerData.lastName, registerData.email);
        if (!usersService.emailIsUnique(registerData.email)) return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);

        usersService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);  // bice token
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody LoginDataDto loginData) throws Exception {
        User user = usersService.loggedUser(loginData.email);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!passwordEncoder.matches(loginData.password, user.getPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        authenticate(loginData.password, loginData.email);

        final UserDetails userDetails = usersServiceImpl.loadUserByUsername(user.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        UserDataDto userData = new UserDataDto(token, user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail());

        return new ResponseEntity<>(userData, HttpStatus.OK);

    }

    private void authenticate(String password, String email) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
