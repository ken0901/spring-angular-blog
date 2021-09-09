package com.ken.blog.controller;

import com.ken.blog.dto.LoginRequest;
import com.ken.blog.dto.RegisterRequest;
import com.ken.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  client -> server
 *  1. Login Request
 *  2. Creates JWT
 *  3. Send JWT to client
 *  4. Client uses JWT to authenticate itself
 *  5. validates JWT
 *  6. Responds to client
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    /**
     *
     * @param registerRequest
     * @return ResponseEntity - http status 200 or something else
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param loginRequest
     * @return JWT Token
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
