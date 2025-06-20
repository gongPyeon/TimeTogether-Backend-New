package timetogeter.context.promise.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import timetogeter.context.promise.domain.entity.PromiseCheck;

import java.util.Optional;

public interface PromiseCheckRepository extends JpaRepository<PromiseCheck, Integer> {
    Optional<PromiseCheck> findByPromiseId(@Param("promiseId") String promiseId);
}
