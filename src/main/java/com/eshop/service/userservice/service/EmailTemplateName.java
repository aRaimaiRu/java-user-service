package com.eshop.service.userservice.service;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate-account"),// java mailing will look up at folder templates
    ;

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
