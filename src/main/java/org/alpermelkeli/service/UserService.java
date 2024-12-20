package org.alpermelkeli.service;

import org.alpermelkeli.model.UserEntity;
import org.alpermelkeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public Optional<UserEntity> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsUser(String email) {
        return userRepository.existsByEmail(email);
    }

}
