package com.eshop.service.userservice.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum BusinessErrorCodes {
    INCORRECT_CURRENT_PASSWORD(BAD_REQUEST.value(), BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(BAD_REQUEST.value(), BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(FORBIDDEN.value(), FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(FORBIDDEN.value(), FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(FORBIDDEN.value(), FORBIDDEN, "Login and / or Password is incorrect"),
    AUTHORIZATION_DENIED(FORBIDDEN.value(), FORBIDDEN, "Authorization denied"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}