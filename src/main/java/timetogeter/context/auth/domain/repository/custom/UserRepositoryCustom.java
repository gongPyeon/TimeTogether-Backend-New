package timetogeter.context.auth.domain.repository.custom;

import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.promise.application.dto.UserInfoDTO;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findByIdAndProviderType(String userId, Provider provider);
    List<UserInfoDTO> findAllNicknameAndImgByUserId(List<String> userId);
}
