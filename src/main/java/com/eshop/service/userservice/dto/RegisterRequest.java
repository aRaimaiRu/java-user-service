package com.eshop.service.userservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterRequest {
    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name cannot be empty")
    private String firstname;
    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name cannot be empty")
    private String lastname;
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @Size(min = 8, message = "Password should be at least 8 characters")
    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
