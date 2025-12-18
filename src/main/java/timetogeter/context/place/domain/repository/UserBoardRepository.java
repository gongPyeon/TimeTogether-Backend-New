package timetogeter.context.place.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timetogeter.context.place.domain.entity.PromisePlace;
import timetogeter.context.place.domain.entity.UserBoard;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserBoardRepository extends JpaRepository<UserBoard, String> {

    @Query("SELECT u FROM UserBoard u WHERE u.placeBoardId = :placeBoardId AND u.userId = :userId")
    Optional<UserBoard> findByPlaceBoardIdAndUserId(@Param("placeBoardId") int placeBoardId, @Param("userId")String userId);

    @Query("SELECT ub.placeBoardId FROM UserBoard ub WHERE ub.userId = :userId AND ub.placeBoardId IN :placeBoardIds")
    Set<Integer> findRatedBoardIds(@Param("userId") String userId, @Param("placeBoardIds") List<Integer> placeBoardIds);
}
