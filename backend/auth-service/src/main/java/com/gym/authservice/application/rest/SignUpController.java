package com.gym.authservice.application.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.authservice.application.dto.SignUpDto;
import com.gym.authservice.domain.User;
import com.gym.authservice.infrastructure.persistence.UserJpaRepository;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("auth")
public class SignUpController {


    private final UserJpaRepository userJpaRepository;

    public SignUpController(UserJpaRepository userJpaRepository
    ) {
        this.userJpaRepository = userJpaRepository;
        System.out.println("the class has initialized");
    }
    
 
    @PostMapping
    public ResponseEntity<User> signUp(@Valid @RequestBody SignUpDto request) {

        User user = new User(request.username, request.password);

        User response = this.userJpaRepository.save(user);

        System.out.println(response);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}