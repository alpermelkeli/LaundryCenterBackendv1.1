package org.alpermelkeli.controller;

import org.alpermelkeli.dto.response.GetUserResponse;
import org.alpermelkeli.model.UserEntity;
import org.alpermelkeli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public ResponseEntity<GetUserResponse> getUser(@RequestParam String email) {
        if(userService.existsUser(email)){
            Optional<UserEntity> user = userService.getUser(email);
            UserEntity currentUser = user.get();
            GetUserResponse response = new GetUserResponse(currentUser.getId(), currentUser.getEmail(), currentUser.getBalance(), currentUser.getRoles());
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

}
