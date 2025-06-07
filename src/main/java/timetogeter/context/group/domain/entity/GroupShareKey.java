package timetogeter.context.group.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupShareKey {

    @Id
    private String groupShareKeyId;

    private String groupId;
    private String encUserId;
    private String encGroupKey;

    private GroupShareKey(String groupId, String encUserId, String encGroupKey) {
        this.groupShareKeyId = UUID.randomUUID().toString();
        this.groupId = groupId;
        this.encUserId = encUserId;
        this.encGroupKey = encGroupKey;
    }

    public static GroupShareKey of(String groupId, String encUserId, String encGroupKey) {
        return new GroupShareKey(groupId, encUserId, encGroupKey);
    }
}
