package timetogeter.context.group.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;

import java.util.List;

@Repository
public interface GroupShareKeyRepository extends JpaRepository<GroupShareKey, String> {
    Long countGroupShareKeyByGroupId(String groupId);

    @Query("SELECT g.encGroupKey FROM GroupShareKey g WHERE g.groupId = :groupId")
    List<String> findEncGroupKeyByGroupId(@Param("groupId") String groupId);

    @Query("SELECT g.encUserId FROM GroupShareKey g WHERE g.groupId = :groupId")
    List<String> findEncUserIdsByGroupId(@Param("groupId") String groupId);

    List<GroupShareKey> findAllByGroupId(String groupId);

    @Query(value = "SELECT enc_group_key_with_user_private_key " +
            "FROM group_share_key " +
            "WHERE group_id = :groupId AND enc_group_member_id = :encGroupMemberId",
            nativeQuery = true)
    String findEncGroupKey(@Param("groupId") String groupId,
                           @Param("encGroupMemberId") String encGroupMemberId);

}
