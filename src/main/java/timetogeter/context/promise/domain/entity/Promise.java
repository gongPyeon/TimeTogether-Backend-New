package timetogeter.context.promise.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promise {
    @Id
    private String promiseId;

    private String groupId;
    private String title;
    private PromiseType type;

    private String promiseImg;
    private String managerId; // userId

    private LocalDate startDate;
    private LocalDate endDate;

    private Promise(String groupId, String title, PromiseType promiseType, String promiseImg, String managerId, LocalDate startDate, LocalDate endDate) {
        this.promiseId = UUID.randomUUID().toString();
        this.groupId = groupId;
        this.title = title;
        this.type = promiseType;
        this.promiseImg = promiseImg;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promise of(String groupId, String title, PromiseType promiseType, String promiseImg, String managerId, LocalDate startDate, LocalDate endDate) {
        return new Promise(groupId, title, promiseType, promiseImg,managerId,startDate,endDate);
    }
   

}
