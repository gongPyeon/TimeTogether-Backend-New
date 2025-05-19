package timetogeter.context.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.custom.UserRepositoryCustom;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    Optional<User> findByUserId(String userId);
    boolean existsByNickname(String nickname);
}

