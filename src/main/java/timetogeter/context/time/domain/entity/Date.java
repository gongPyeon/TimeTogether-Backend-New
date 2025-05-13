package timetogeter.context.time.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogeter.context.time.domain.vo.WeekDay;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dateId;

    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;
    private int day; // 1-31
}
