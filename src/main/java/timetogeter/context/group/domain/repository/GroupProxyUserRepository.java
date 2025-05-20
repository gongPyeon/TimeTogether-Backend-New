package timetogeter.context.group.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import java.util.*;


@Repository
public interface GroupProxyUserRepository extends JpaRepository<GroupProxyUser, String> {
    @Query("select g.encGroupId from GroupProxyUser g where g.userId = :userId")
    List<String> findEncGroupIdsByUserId(@Param("userId") String userId);
}