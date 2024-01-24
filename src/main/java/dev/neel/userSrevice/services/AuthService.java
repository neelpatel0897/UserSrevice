package dev.neel.userSrevice.services;

import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.util.MultiValueMapAdapter;
import org.apache.commons.lang3.RandomStringUtils;
import dev.neel.userSrevice.dtos.UserDto;
import dev.neel.userSrevice.repositories.SessionRepository;
import dev.neel.userSrevice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import dev.neel.userSrevice.models.*;

@Service
public class AuthService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository,SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDto signup(String email, String password) {

        //Creating new user
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        //Saving new user to database
        User savedUser = userRepository.save(user);


        //Return new userDto based on saved user
        return UserDto.from(savedUser);

        
        
    }

    public ResponseEntity<UserDto> login(String email, String password) throws Exception {
        
        //Findinf user by email in database

        Optional<User> userOptional = userRepository.findByEmail(email);

        //checking the user is available or not
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();

        //checking the password is correct or not
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
            
        }

        
        //Generating token
        String token = RandomStringUtils.randomAlphanumeric(30);

        // Create a test key suitable for the desired HMAC-SHA algorithm:
        // MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256
        // SecretKey key = Jwts.builder().alg.key().build();
        SecretKey testKey = Jwts.SIG.HS512.key().build();

        // String message = "Hello World!";
        // //user_id
        // //email
        // //List of roles
        // byte[] content = message.getBytes(StandardCharsets.UTF_8);
        Map<String,Object> jsonForJwt=new HashMap<String,Object>();
        jsonForJwt.put("email",user.getEmail());
        jsonForJwt.put("roles",user.getRoles());
        jsonForJwt.put("createdAt", new Date());
        jsonForJwt.put("expiryAt", LocalDate.now().plusDays(3).toEpochDay());

        token= Jwts.builder().claims(jsonForJwt).signWith(testKey).compact();

        // Create the compact JWS:
        // String jws = Jwts.builder().content(content, "text/plain").signWith(key, alg).compact();

        // // Parse the compact JWS:
        // content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();

        // assert message.equals(new String(content, StandardCharsets.UTF_8));

        //Creating session and storing it in database
        Session session = new Session();
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());

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

       Session session = sessionOptional.get();

       if(session.getStatus()==SessionStatus.ENDED){
           return SessionStatus.ENDED;
       }
        Jws<Claims> claimsJws = Jwts.parser()
                .build()
                .parseSignedClaims(token);

        String email = (String) claimsJws.getPayload().get("email");
        List<Role> roles = (List<Role>) claimsJws.getPayload().get("roles");
        Date createdAt = (Date) claimsJws.getPayload().get("createdAt");

        if (createdAt.before(new Date())) {
            return SessionStatus.ENDED;
        }
        return SessionStatus.ACTIVE;
    }
    
}