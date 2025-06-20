package timetogeter.context.promise.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.entity.PromiseShareKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromiseShareKeyRepository extends JpaRepository<PromiseShareKey, String> {

    List<PromiseShareKey> findByPromiseId(String promiseId);

    @Query("SELECT p.encUserId FROM PromiseShareKey p WHERE p.promiseId = :promiseId")
    List<String> findUserIdsByPromiseId(String promiseId);

    @Query("SELECT p FROM PromiseShareKey p WHERE p.encPromiseKey = :encPromiseKey")
    Optional<PromiseShareKey> findByEncPromiseKey(String encPromiseKey);
}
