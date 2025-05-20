package timetogeter.context.group.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;

import java.util.List;

@Repository
public interface GroupShareKeyRepository extends JpaRepository<GroupShareKey, String> {
    Long countGroupShareKeyByGroupId(String groupId);

    String findEncGroupKeyByGroupId(String groupId);

    List<String> findEncUserIdsByGroupId(String groupId);
}
