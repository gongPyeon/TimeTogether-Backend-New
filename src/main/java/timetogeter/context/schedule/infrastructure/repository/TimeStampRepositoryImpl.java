package timetogeter.context.schedule.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import timetogeter.context.schedule.domain.entity.QTimeStamp;
import timetogeter.context.schedule.domain.repository.custom.TimeStampRepositoryCustom;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TimeStampRepositoryImpl implements TimeStampRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findTimeStampsByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        QTimeStamp t = QTimeStamp.timeStamp;

        return queryFactory
                .select(t.encTimeStamp)
                .from(t)
                .where(
                        t.userId.eq(userId),
                        t.timeStampInfo.between(startDate, endDate)
                )
                .fetch();
    }
}
