package com.gym.authservice.application.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.authservice.application.dto.SignUpDto;
import com.gym.authservice.domain.User;
import com.gym.authservice.domain.enums.Role;
import com.gym.authservice.infrastructure.persistence.UserJpaRepository;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController()
@RequestMapping("/auth")
public class SignUpController {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpController(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
        System.out.println("the class has initialized");
    }
    
 
    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@Valid @RequestBody SignUpDto request) {
        var role = request.role != null ? request.role : Role.USER;
        User user = new User(request.username, passwordEncoder.encode(request.password), role);

        User response = this.userJpaRepository.save(user);

        System.out.println(response);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getUsers(){
        List<String> response = this.userJpaRepository.findAll().stream().map(user -> user.getUsername()).toList();
        return ResponseEntity.ok(response);
    }
}