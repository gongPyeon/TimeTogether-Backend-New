package timetogeter.context.promise.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.entity.PromiseProxyUser;

import java.util.List;

@Repository
public interface PromiseProxyUserRepository extends JpaRepository<PromiseProxyUser, String> {

    @Query(value = """
        SELECT enc_promise_id
        FROM promise_proxy_user
        WHERE user_id = :userId
        """, nativeQuery = true)
    List<String> findPromiseIdsByUserId(@Param("userId") String userId);
}
