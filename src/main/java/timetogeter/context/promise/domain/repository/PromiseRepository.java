package timetogeter.context.promise.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.repository.custom.PromiseRepositoryCustom;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromiseRepository extends JpaRepository<Promise, String>, PromiseRepositoryCustom {

    @Query(value = "SELECT * FROM promise WHERE promise_id IN (:promiseIdList)", nativeQuery = true)
    List<Promise> findByPromiseIdIn(@Param("promiseIdList") List<String> promiseIdList);

    @Query("SELECT p.managerId FROM Promise p WHERE p.promiseId = :promiseId")
    Optional<String> findMangerById(@Param("promiseId")String promiseId);
}
