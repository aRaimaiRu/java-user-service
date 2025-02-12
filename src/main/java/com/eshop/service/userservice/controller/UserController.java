package com.eshop.service.userservice.controller;

import com.eshop.service.userservice.model.User;
import com.eshop.service.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("users")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<User>> list() {
        List<User> user = userService.listUsers();
        return ResponseEntity.ok(user);
    }

}
