package timetogeter.context.auth.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google"),
    GENERAL("general");

    private final String text;
}
