package com.pro.oauth2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 14440)
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String email;
    private String refreshToken;
}
