package timetogeter.context.schedule.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromiseShareKey {
    @Id
    private String promiseShareKeyId;

    private String promiseId;
    private String scheduleId;
    private String encUserId;
    private String encPromiseKey;

    PromiseShareKey(String promiseId, String scheduleId, String encUserId, String encPromiseKey) {
        this.promiseShareKeyId = UUID.randomUUID().toString();
        this.promiseId = promiseId;
        this.scheduleId = scheduleId;
        this.encUserId = encUserId;
        this.encPromiseKey = encPromiseKey;
    }
}
