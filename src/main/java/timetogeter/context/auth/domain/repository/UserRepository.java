package timetogeter.context.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.custom.UserRepositoryCustom;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    Optional<User> findByUserId(String userId);

    @Query("SELECT u.nickname FROM User u WHERE u.userId IN :userIds")
    List<String> findAllNicknameByUserId(@Param("userIds") List<String> userIds);
}

