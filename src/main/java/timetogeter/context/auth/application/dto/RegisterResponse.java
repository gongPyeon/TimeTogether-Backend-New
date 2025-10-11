package timetogeter.context.auth.application.dto;

import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;

public record RegisterResponse(String userId,
                               String nickname,
                               String email,
                               String password,
                               Provider provider,
                               Role role,
                               String imgIv,
                               String emailIv,
                               String phoneIv) {

    public static RegisterResponse from(User user) {
        return new RegisterResponse(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getPassword(),
                user.getProvider(),
                user.getRole(),
                user.getImgIv(),
                user.getEmailIv(),
                user.getPhoneIv()
        );
    }
}
