package timetogeter.context.schedule.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @Column(unique = true, nullable = false)
    private String scheduleId;

    private String title;
    private String content;

    private String type; // promiseType을 써도 괜찮을지
    private String place;
    private String placeUrl;

    private LocalDateTime startDateTime; //추가함 (확정된 일정에 대해 시작시각, 종료시각 필드가 없어서)
    private LocalDateTime endDateTime; //추가함

    private String groupId; // TODO: 추가함
    private String groupName; // TODO: 추가함
    private String promiseId; // TODO: 추가함

    public static Schedule of(
            String title,
            String content,
            String type,
            String place,
            String placeUrl,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String groupId,
            String groupName,
            String promiseId
    ) {
        Schedule schedule = new Schedule();
        schedule.scheduleId = UUID.randomUUID().toString();
        schedule.title = title;
        schedule.content = content;
        schedule.type = type;
        schedule.place = place;
        schedule.placeUrl = placeUrl;
        schedule.startDateTime = startDateTime;
        schedule.endDateTime = endDateTime;
        schedule.groupId = groupId;
        schedule.groupName = groupName;
        schedule.promiseId = promiseId;
        return schedule;
    }

    public void update(
            String title,
            String content,
            String type,
            String place,
            String placeUrl,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (type != null) this.type = type;
        if (place != null) this.place = place;
        if (placeUrl != null) this.placeUrl = placeUrl;
        if (startDateTime != null) this.startDateTime = startDateTime;
        if (endDateTime != null) this.endDateTime = endDateTime;
    }

}
