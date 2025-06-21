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
    private String purpose;

    //private String type; // promiseType을 써도 괜찮을지
    private int placeId;
    private String groupId;

    public static Schedule of(
            String encStartTimeEndTime,
            String title,
            String content,
            String purpose,
            int placeId,
            String groupId
    ) {
        return Schedule.builder()
                .scheduleId(encStartTimeEndTime)
                .title(title)
                .content(content)
                .purpose(purpose)
                .placeId(placeId)
                .groupId(groupId)
                .build();
    }

    public void update(
            String title,
            String content,
            String purpose,
            Integer placeId,
            String groupId
    ) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (purpose != null) this.purpose = purpose;
        if (placeId != null) this.placeId = placeId;
        if (groupId != null) this.groupId = groupId;
    }



}
