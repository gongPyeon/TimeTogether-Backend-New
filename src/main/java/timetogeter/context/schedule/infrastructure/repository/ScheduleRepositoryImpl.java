package timetogeter.context.schedule.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import timetogeter.context.auth.domain.repository.custom.UserRepositoryCustom;
import timetogeter.context.schedule.domain.entity.QSchedule;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.custom.ScheduleRepositoryCustom;

import java.util.List;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ScheduleRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    // TODO: 사람 이름이 아닌, 그룹 이름으로 조회
    // TODO: groupName 변경 시 문제! (정규화문제) - HOW?
    @Override
    public List<Schedule> searchByQueryAndFilters(String query, List<String> filters) {
        QSchedule s = QSchedule.schedule;
        BooleanBuilder builder = new BooleanBuilder();

        if (filters.contains("title")) {
            builder.or(s.title.containsIgnoreCase(query));
        }
        if (filters.contains("place")) {
            builder.or(s.place.containsIgnoreCase(query));
        }
        if (filters.contains("group")) {
            builder.or(s.groupName.containsIgnoreCase(query));
        }

        return queryFactory
                .selectFrom(s)
                .where(builder)
                .fetch();
    }

}
