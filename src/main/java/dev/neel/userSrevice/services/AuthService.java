package dev.neel.userSrevice.services;

import org.springframework.http.*;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;
import org.apache.commons.lang3.RandomStringUtils;
import dev.neel.userSrevice.dtos.UserDto;
import dev.neel.userSrevice.repositories.SessionRepository;
import dev.neel.userSrevice.repositories.UserRepository;
import dev.neel.userSrevice.models.*;

@Service
public class AuthService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository,SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public UserDto signup(String email, String password) {

        //Creating new user
        User user = new User(email, password);

        //Saving new user to database
        User savedUser = userRepository.save(user);


        //Return new userDto based on saved user
        UserDto userDto = new UserDto();
        userDto.setEmail(savedUser.getEmail());
        userDto.setRoles(savedUser.getRoles());

        return userDto;

        
        
    }

    public ResponseEntity<UserDto> login(String email, String password) {
        
        //Findinf user by email in database

        Optional<User> userOptional = userRepository.findByEmail(email);

        //checking the user is available or not
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();

        //checking the password is correct or not
        if (!user.getPassword().equals(password)) {
            return null;
        }

        
        //Generating token
        String token = RandomStringUtils.randomAlphanumeric(30);

        //Creating session and storing it in database
        Session session = new Session();
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = new UserDto();

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);



        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);


        
        return response;

    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setStatus(SessionStatus.ENDED);
        sessionRepository.save(session);



        return ResponseEntity.ok().build();
    }

    public SessionStatus validateToken(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return SessionStatus.ENDED;
        }

       

        return SessionStatus.ACTIVE;
    }
    
}