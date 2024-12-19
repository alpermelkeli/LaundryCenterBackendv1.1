package org.alpermelkeli.controller;

import org.alpermelkeli.dto.RegisterDto;
import org.alpermelkeli.model.Role;
import org.alpermelkeli.model.UserEntity;
import org.alpermelkeli.repository.RoleRepository;
import org.alpermelkeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("E-mail has been taken.", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();

        user.setEmail(registerDto.getEmail());

        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepository.findByName("USER").get();

        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}