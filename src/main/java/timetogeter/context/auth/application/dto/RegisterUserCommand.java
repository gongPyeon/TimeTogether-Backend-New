package timetogeter.context.auth.application.dto;

import jakarta.validation.constraints.NotNull;
import timetogeter.context.auth.domain.vo.Gender;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;

public record RegisterUserCommand(String userId,
                                  String email,
                                  String telephone,
                                  String nickname,
                                  Provider provider,
                                  Role role,
                                  String age,
                                  Gender gender,
                                  String imgIv,
                                  String emailIv,
                                  String phoneIv
                                  ){

}
