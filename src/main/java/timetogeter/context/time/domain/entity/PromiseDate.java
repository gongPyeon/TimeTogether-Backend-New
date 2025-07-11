package timetogeter.context.time.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogeter.context.time.domain.vo.WeekDay;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromiseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dateId;

    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;
    private LocalDate date; // 1-31
    private String promiseId;

    public PromiseDate(WeekDay weekDay, LocalDate date, String promiseId) {
        this.weekDay = weekDay;
        this.date = date;
        this.promiseId = promiseId;
    }
}
