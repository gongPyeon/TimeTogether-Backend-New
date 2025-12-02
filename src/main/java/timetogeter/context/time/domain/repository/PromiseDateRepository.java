package timetogeter.context.time.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timetogeter.context.time.domain.entity.PromiseDate;

import java.time.LocalDate;
import java.util.Optional;

public interface PromiseDateRepository extends JpaRepository<PromiseDate, Integer> {

    @Query("SELECT d.dateId FROM PromiseDate d WHERE d.promiseId = :promiseId")
    int findIdByPromiseId(@Param("promiseId")String promiseId);

    Optional<PromiseDate> findByPromiseIdAndDate(
            @Param("promiseId")String promiseId, @Param("date")LocalDate date);
}
