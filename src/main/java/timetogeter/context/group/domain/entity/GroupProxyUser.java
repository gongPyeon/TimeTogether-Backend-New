package timetogeter.context.group.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupProxyUser {
    @Id
    private String groupProxyId;

    private String userId;
    private String encGroupId;

    @CreationTimestamp
    private LocalDateTime timestamp;
    private String encGroupMemberId;

    GroupProxyUser(String userId, String encGroupId, LocalDateTime timestamp, String encGroupMemberId) {
        this.groupProxyId = UUID.randomUUID().toString();
        this.userId = userId;
        this.encGroupId = encGroupId;
        this.timestamp = timestamp;
        this.encGroupMemberId = encGroupMemberId;
    }
}
