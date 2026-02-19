package com.gym.authservice.application.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.authservice.application.dto.SignUpDto;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("auth")
public class SignUpController {

    public SignUpController() {
        System.out.println("the class has initialized");
    }
    
 
    @PostMapping
    public ResponseEntity<SignUpDto> signUp(@Valid @RequestBody SignUpDto request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(request);
    }
}