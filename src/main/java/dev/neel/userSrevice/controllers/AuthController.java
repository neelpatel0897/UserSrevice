package dev.neel.userSrevice.controllers;

import org.springframework.web.bind.annotation.RestController;

import dev.neel.userSrevice.dtos.LoginRequestDto;
import dev.neel.userSrevice.dtos.LogoutRequestDto;
import dev.neel.userSrevice.dtos.SignUpRequestDto;

import dev.neel.userSrevice.dtos.UserDto;
import dev.neel.userSrevice.dtos.ValidateTokenRequestDto;
import dev.neel.userSrevice.models.SessionStatus;
import dev.neel.userSrevice.services.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")     
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception{
        return authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
          
        

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        return authService.logout(request.getToken(), request.getUserId());
    }


    @PostMapping("/signup")    
    public ResponseEntity<UserDto> signup(@RequestBody SignUpRequestDto request){
            UserDto userDto =authService.signup(request.getEmail(), request.getPassword());            
            return new ResponseEntity<>(userDto,HttpStatus.OK);

    }

    @PostMapping("/validate")      
    public SessionStatus validateToken(ValidateTokenRequestDto request){
        SessionStatus status = authService.validateToken(request.getToken(), request.getUserId());

        
        return status;

    }

   
}