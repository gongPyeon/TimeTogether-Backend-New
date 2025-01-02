package com.pro.oauth2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_GUEST("ROLE_GUEST"), ROLE_USER("ROLE_USER");

    private final String description;
}
