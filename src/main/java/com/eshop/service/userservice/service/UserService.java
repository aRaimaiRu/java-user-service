package com.eshop.service.userservice.service;

import com.eshop.service.userservice.model.User;
import com.eshop.service.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> listUsers() {
            return userRepository.findAll();
        }

}
