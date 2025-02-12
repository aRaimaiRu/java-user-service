package com.eshop.service.userservice.controller;

import com.eshop.service.userservice.dto.AuthenticationRequest;
import com.eshop.service.userservice.dto.AuthenticationResponse;
import com.eshop.service.userservice.dto.RegisterRequest;
import com.eshop.service.userservice.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) throws MessagingException {
        service.register(registerRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @GetMapping("/activate-account")
    public void confirmAccount(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
    }
}
