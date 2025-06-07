package timetogeter.context.schedule.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import timetogeter.context.promise.domain.vo.PromiseType;

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
    private String groupId; // TODO: 추가함
    private String promiseId; // TODO: 추가함
}
