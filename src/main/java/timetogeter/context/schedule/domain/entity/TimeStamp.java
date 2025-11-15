package timetogeter.context.schedule.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timestampId;
    private String encTimeStamp; // 년 월 일 시간
    private LocalDate timeStampInfo; // 년 월 일
    private String userId;

    public static TimeStamp of(String encTimeStamp, LocalDate timeStampInfo, String userId) {
        return TimeStamp.builder()
                .encTimeStamp(encTimeStamp)
                .timeStampInfo(timeStampInfo)
                .userId(userId)
                .build();
    }
}
