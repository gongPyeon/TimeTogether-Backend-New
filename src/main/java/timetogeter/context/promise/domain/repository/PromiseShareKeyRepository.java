package timetogeter.context.promise.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.entity.PromiseShareKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromiseShareKeyRepository extends JpaRepository<PromiseShareKey, String> {
    @Query(value = "SELECT schedule_id FROM promise_share_key WHERE enc_promise_key IN (:encPromiseKeyList)", nativeQuery = true)
    List<String> findScheduleIdsByEncPromiseKeyList(@Param("encPromiseKeyList") List<String> encPromiseKeyList);

    List<PromiseShareKey> findByPromiseId(@Param("promiseId")String promiseId);

    @Query(value = "SELECT * FROM promise_share_key WHERE enc_promise_key IN (:encPromiseKeyList)", nativeQuery = true)
    List<PromiseShareKey> findByEncPromiseKeyIn(@Param("encPromiseKeyList") List<String> encPromiseKeyList);

    @Query(value = "SELECT * FROM promise_share_key WHERE enc_promise_key = :encPromiseKey AND enc_user_id = :encUserId", nativeQuery = true)
    Optional<PromiseShareKey> findByEncPromiseKeyAndEncUserId(
            @Param("encPromiseKey") String encPromiseKey,
            @Param("encUserId") String encUserId
    );


    @Query("SELECT p.encUserId FROM PromiseShareKey p WHERE p.promiseId = :promiseId")
    List<String> findUserIdsByPromiseId(@Param("promiseId")String promiseId);

    @Query("SELECT p FROM PromiseShareKey p WHERE p.encPromiseKey = :encPromiseKey")
    Optional<PromiseShareKey> findByEncPromiseKey(@Param("encPromiseKey")String encPromiseKey);

    @Modifying
    @Query("DELETE FROM PromiseProxyUser p WHERE p.encPromiseId = :encPromiseId")
    void deleteAllByEncPromiseId(@Param("encPromiseId") String encPromiseId);
}
