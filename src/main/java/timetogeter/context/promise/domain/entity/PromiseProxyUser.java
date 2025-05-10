package timetogeter.context.promise.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromiseProxyUser {
    @Id
    private String promiseProxyId;

    private String userId;
    private String encPromiseId;

    @CreationTimestamp
    private LocalDateTime timestamp;
    private String encPromiseMemberId;

    PromiseProxyUser(String userId, String encPromiseId, LocalDateTime timestamp, String encPromiseMemberId) {
        this.promiseProxyId = UUID.randomUUID().toString();
        this.userId = userId;
        this.encPromiseId = encPromiseId;
        this.timestamp = timestamp;
        this.encPromiseMemberId = encPromiseMemberId;
    }
}
