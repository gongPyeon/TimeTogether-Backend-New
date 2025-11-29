package timetogeter.context.time.infrastructure.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import timetogeter.context.auth.domain.entity.QUser;
import timetogeter.context.time.application.dto.DailyTimeDTO;
import timetogeter.context.time.application.dto.TimeSlotDTO;
import timetogeter.context.time.domain.entity.QPromiseDate;
import timetogeter.context.time.domain.entity.QPromiseTime;
import timetogeter.context.time.domain.repository.custom.PromiseTimeRepositoryCustom;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PromiseTimeRepositoryImpl implements PromiseTimeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<DailyTimeDTO> findAllWithDailyTimesByPromiseId(String promiseId) {
        QPromiseDate d = QPromiseDate.promiseDate;
        QPromiseTime t = QPromiseTime.promiseTime;

        List<Tuple> result = queryFactory
                .select(d.date, t.time)
                .from(d)
                .join(t).on(d.dateId.eq(t.dateId))
                .where(d.promiseId.eq(promiseId))
                .orderBy(d.date.asc(), t.time.asc())
                .fetch();

        Map<LocalDate, List<TimeSlotDTO>> grouped = result.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(d.date),
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        tuple -> tuple.get(t.time),
                                        Collectors.counting()
                                ),
                                timeMap -> timeMap.entrySet().stream()
                                        .map(entry -> new TimeSlotDTO(
                                                entry.getKey(),
                                                entry.getValue().intValue()
                                        ))
                                        .collect(Collectors.toList())
                        )
                ));

        return grouped.entrySet().stream()
                .map(entry -> new DailyTimeDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DailyTimeDTO::date))
                .toList();
    }

    @Override
    public List<String> findUsersAtTime(String promiseId, LocalDate date, LocalTime time) {
        QUser u = QUser.user;
        QPromiseDate d = QPromiseDate.promiseDate;
        QPromiseTime t = QPromiseTime.promiseTime;

        List<String> names = queryFactory
                .select(u.nickname)
                .from(d)
                .join(t).on(d.dateId.eq(t.dateId))
                .join(u).on(t.userId.eq(u.userId))
                .where(
                        d.promiseId.eq(promiseId),
                        d.date.eq(date),
                        t.time.eq(time)
                )
                .fetch();

        return names;
    }
}
