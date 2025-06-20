package timetogeter.context.time.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timetogeter.context.time.domain.entity.PromiseCheck;

import java.util.Optional;

public interface PromiseCheckRepository extends JpaRepository<PromiseCheck, Integer> {

    @Query("SELECT p FROM PromiseCheck p WHERE p.promiseId = :promiseId")
    Optional<PromiseCheck> isConfirmedById(@Param("promiseId")String promiseId);
}
