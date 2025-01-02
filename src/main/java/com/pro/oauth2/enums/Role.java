package com.pro.oauth2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_QUEST("게스트"), ROLE_USER("사용자");

    private final String description;
}
