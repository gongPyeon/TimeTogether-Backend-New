package timetogeter.context.group.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;

@Repository
public interface GroupShareKeyRepository extends JpaRepository<GroupShareKey, String> {
    Long countGroupShareKeyByGroupId(String groupId);
}
