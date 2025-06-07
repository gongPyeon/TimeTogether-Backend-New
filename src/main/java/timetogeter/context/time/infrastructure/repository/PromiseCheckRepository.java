package timetogeter.context.time.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import timetogeter.context.time.domain.entity.PromiseCheck;

import java.util.Optional;

public interface PromiseCheckRepository extends JpaRepository<PromiseCheck, Integer> {

    @Query("SELECT p FROM PromiseCheck p")
    Optional<PromiseCheck> isConfirmedById(String promiseId);
}
