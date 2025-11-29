package timetogeter.context.schedule.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import timetogeter.context.auth.domain.repository.custom.UserRepositoryCustom;
import timetogeter.context.group.domain.entity.QGroup;
import timetogeter.context.place.domain.entity.QPlaceBoard;
import timetogeter.context.schedule.application.dto.PromiseDetailDTO;
import timetogeter.context.schedule.application.dto.response.PromiseDetailResDTO;
import timetogeter.context.schedule.domain.entity.QSchedule;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.custom.ScheduleRepositoryCustom;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Schedule> searchByQueryAndFilters(String query) {
        QSchedule s = QSchedule.schedule;
        QPlaceBoard p = QPlaceBoard.placeBoard;

        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        BooleanBuilder searchConditions = new BooleanBuilder();

        searchConditions.or(s.title.containsIgnoreCase(query));
        searchConditions.or(p.placeName.containsIgnoreCase(query));

        return queryFactory
                .selectFrom(s)
                .leftJoin(p).on(s.placeId.eq(p.placeBoardId))
                .where(searchConditions)
                .distinct()
                .fetch();
    }

    @Override
    public PromiseDetailDTO findDetailByScheduleId(String scheduleId) {
        QSchedule s = QSchedule.schedule;
        QPlaceBoard p = QPlaceBoard.placeBoard;
        QGroup g = QGroup.group;

        return queryFactory
                .select(Projections.constructor(PromiseDetailDTO.class,
                        s.scheduleId,
                        s.title,
                        s.purpose,
                        p.placeName,
                        g.groupName
                ))
                .from(s)
                .leftJoin(p).on(s.placeId.eq(p.placeBoardId))
                .leftJoin(g).on(s.groupId.eq(g.groupId))
                .where(s.scheduleId.eq(scheduleId))
                .fetchOne();
    }

}
