package com.pro.oauth2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_GUEST("GUEST"), ROLE_USER("USER");

    private final String description;
}
