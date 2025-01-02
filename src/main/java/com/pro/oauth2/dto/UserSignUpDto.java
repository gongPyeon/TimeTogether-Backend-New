package com.pro.oauth2.dto;

import com.pro.oauth2.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {
    private String email;
    private String password;
    private String name;
    private Role role = Role.ROLE_USER;
}

