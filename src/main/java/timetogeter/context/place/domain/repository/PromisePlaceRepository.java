package timetogeter.context.place.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timetogeter.context.place.domain.entity.PromisePlace;

import java.util.List;
import java.util.Optional;

public interface PromisePlaceRepository extends JpaRepository<PromisePlace, String> {
    Optional<PromisePlace> findByPlaceId(int placeId);

    @Query("SELECT p FROM PromisePlace p WHERE p.promiseId = :promiseId")
    Page<PromisePlace> findByPromiseId(@Param("promiseId") String promiseId, PageRequest pageRequest);

    @Query("SELECT p FROM PromisePlace p WHERE p.placeId IN (:placeIds)")
    List<PromisePlace> findByPlaceIdIn(@Param("placeIds") List<Integer> placeIds);

    @Query("SELECT p.placeAddr FROM PromisePlace p WHERE p.promiseId = :promiseId")
    List<String> findAiPlaceIdsByPromiseId(@Param("promiseId") String promiseId);
}
