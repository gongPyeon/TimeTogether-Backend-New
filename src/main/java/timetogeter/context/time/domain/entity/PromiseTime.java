package timetogeter.context.time.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromiseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timeId;

    private int dateId;
    private String promiseId;

    private LocalTime time;
    private String userId;

    public PromiseTime(int dateId, LocalTime time, String userId, String promiseId) {
        this.dateId = dateId;
        this.time = time;
        this.userId = userId;
        this.promiseId = promiseId;
    }
}
