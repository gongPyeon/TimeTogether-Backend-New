package timetogeter.context.place.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timetogeter.context.place.domain.entity.PromisePlace;
import timetogeter.context.place.domain.entity.UserBoard;

import java.util.Optional;

public interface UserBoardRepository extends JpaRepository<UserBoard, String> {

    @Query("SELECT u FROM UserBoard u WHERE u.placeBoardId = :placeBoardId AND u.userId = :userId")
    Optional<UserBoard> findByPlaceBoardIdAndUserId(@Param("placeBoardId") int placeBoardId, @Param("userId")String userId);
}
