package timetogeter.context.time.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import timetogeter.context.time.domain.entity.PromiseDate;
import timetogeter.context.time.domain.entity.PromiseTime;
import timetogeter.context.time.domain.repository.custom.PromiseTimeRepositoryCustom;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface PromiseTimeRepository extends JpaRepository<PromiseTime, Integer>, PromiseTimeRepositoryCustom {
    Optional<PromiseTime> findByDateIdAndTimeAndUserId(
            @Param("dateId")int dateId, @Param("time") LocalTime time, @Param("userId")String userId);
}
