package timetogeter.context.auth.domain.repository.custom;

import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.vo.Provider;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findByIdAndProviderType(String userId, Provider provider);
}
