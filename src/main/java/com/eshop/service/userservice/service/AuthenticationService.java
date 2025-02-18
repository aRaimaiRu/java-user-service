package com.eshop.service.userservice.service;

import com.eshop.common_lib.constant.RoleEnum;
import com.eshop.service.userservice.dto.AuthenticationRequest;
import com.eshop.service.userservice.dto.AuthenticationResponse;
import com.eshop.service.userservice.dto.RegisterRequest;
import com.eshop.service.userservice.exception.*;
import com.eshop.service.userservice.model.Role;
import com.eshop.service.userservice.model.Token;
import com.eshop.service.userservice.model.User;
import com.eshop.service.userservice.repository.RoleRepository;
import com.eshop.service.userservice.repository.TokenRepository;
import com.eshop.service.userservice.repository.UserRepository;
import com.eshop.service.userservice.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${server.url}")
    private String confirmationUrl;

    public void register(RegisterRequest registerRequest) throws MessagingException {
        var userRole = roleRepository.findByName(RoleEnum.USER).orElseThrow(() -> new RoleNotFoundException());
        List<Role> roles = List.of(userRole);
        Optional<User> existUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existUser.isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(roles)
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        // send email
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(user.getEmail(), user.getName(), EmailTemplateName.ACTIVATE_ACCOUNT, confirmationUrl, newToken, "Account Activation");
    }

    private String generateAndSaveActivationToken(User user) {
        // generate token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            codeBuilder.append(characters.charAt(random.nextInt(characters.length()))); // generate random number
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullName", user.getName());
        var token = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(token).build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new TokenExpiredException("Activation code expired. New activation code sent to your email");

        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEnabled(true);
        user.setAccountLocked(false);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
