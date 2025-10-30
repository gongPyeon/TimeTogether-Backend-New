package timetogeter.context.promise.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.entity.PromiseProxyUser;
import java.util.Optional;
import java.util.List;

@Repository
public interface PromiseProxyUserRepository extends JpaRepository<PromiseProxyUser, String> {
    Optional<PromiseProxyUser> findByEncPromiseId(String encPromiseId);
    @Query(value = """
        SELECT enc_promise_id
        FROM promise_proxy_user
        WHERE user_id = :userId
        """, nativeQuery = true)
    List<String> findPromiseIdsByUserId(@Param("userId") String userId);


    @Modifying
    @Query("DELETE FROM PromiseShareKey p WHERE p.encUserId IN :userIds")
    void deleteAllByUserIdIn(@Param("userIds") List<String> userIds);
}
