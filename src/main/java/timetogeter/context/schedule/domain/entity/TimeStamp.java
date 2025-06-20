package timetogeter.context.schedule.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeStamp {

    @Id
    private String encTimeStamp; // 년 월 일 시간
    private LocalDate timeStampInfo; // 년 월 일
    private String userId;

}
