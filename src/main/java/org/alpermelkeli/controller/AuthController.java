package org.alpermelkeli.controller;

import org.alpermelkeli.dto.request.TokenRefreshRequestDto;
import org.alpermelkeli.dto.response.AuthResponseDto;
import org.alpermelkeli.dto.request.LoginDto;
import org.alpermelkeli.dto.request.RegisterDto;
import org.alpermelkeli.dto.response.RefreshTokenResponseDto;
import org.alpermelkeli.model.RefreshTokenEntity;
import org.alpermelkeli.model.RoleEntity;
import org.alpermelkeli.model.UserEntity;
import org.alpermelkeli.repository.RefreshTokenRepository;
import org.alpermelkeli.repository.RoleRepository;
import org.alpermelkeli.repository.UserRepository;
import org.alpermelkeli.security.jwt.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("E-mail has been taken.", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();

        user.setEmail(registerDto.getEmail());

        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        user.setBalance(0.0);

        RoleEntity roleEntity = roleRepository.findByName("USER").get();

        user.setRoles(Collections.singletonList(roleEntity));

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        if(!userRepository.existsByEmail(loginDto.getEmail())) {
            return new ResponseEntity<>(new AuthResponseDto(null, null), HttpStatus.NOT_FOUND);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        RefreshTokenEntity refreshTokenEntity = jwtGenerator.generateRefreshToken(authentication);

        UserEntity user = userRepository.findByEmail(loginDto.getEmail()).get();

        refreshTokenEntity.setUser(user);

        refreshTokenRepository.save(refreshTokenEntity);

        return new ResponseEntity<>(new AuthResponseDto(token, refreshTokenEntity.getRefreshToken()), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        // Check if the refresh token exists in the database
        if(refreshTokenRepository.existsByRefreshToken(tokenRefreshRequestDto.getRefreshToken())){

            UserEntity user = refreshTokenRepository.findByRefreshToken(tokenRefreshRequestDto.getRefreshToken()).getUser();

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Check if the refresh token is valid
            if(jwtGenerator.validateRefreshToken(tokenRefreshRequestDto.getRefreshToken())) {
                return new ResponseEntity<>(new
                        RefreshTokenResponseDto
                        (jwtGenerator.generateToken(authentication)),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
