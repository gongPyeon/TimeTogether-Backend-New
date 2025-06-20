package timetogeter.context.schedule.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.schedule.domain.entity.PromiseShareKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromiseShareKeyRepository extends JpaRepository<PromiseShareKey, String> {
    @Query(value = "SELECT schedule_id FROM promise_share_key WHERE enc_promise_key IN (:encPromiseKeyList)", nativeQuery = true)
    List<String> findScheduleIdsByEncPromiseKeyList(@Param("encPromiseKeyList") List<String> encPromiseKeyList);

    List<PromiseShareKey> findByPromiseId(String promiseId);

    @Query(value = "SELECT * FROM promise_share_key WHERE enc_promise_key IN (:encPromiseKeyList)", nativeQuery = true)
    List<PromiseShareKey> findByEncPromiseKeyIn(@Param("encPromiseKeyList") List<String> encPromiseKeyList);

    @Query(value = "SELECT * FROM promise_share_key WHERE enc_promise_key = :encPromiseKey AND enc_user_id = :encUserId", nativeQuery = true)
    Optional<PromiseShareKey> findByEncPromiseKeyAndEncUserId(
            @Param("encPromiseKey") String encPromiseKey,
            @Param("encUserId") String encUserId
    );

}
