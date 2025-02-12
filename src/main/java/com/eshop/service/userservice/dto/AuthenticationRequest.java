package com.eshop.service.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AuthenticationRequest {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @Size(min = 8, message = "Password should be at least 8 characters")
    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
