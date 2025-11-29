package timetogeter.context.promise.infrastructure.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.repository.custom.PromiseShareRepositoryCustom;

import java.util.List;

import static timetogeter.context.promise.domain.entity.QPromiseShareKey.promiseShareKey;

@Repository
@RequiredArgsConstructor
public class PromiseShareKeyRepositoryImpl implements PromiseShareRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<String> findNamesByScheduleId(String scheduleId) {

        var promiseIdSubQuery = JPAExpressions
                .select(promiseShareKey.promiseId)
                .from(promiseShareKey)
                .where(promiseShareKey.scheduleId.eq(scheduleId))
                .distinct();

        return queryFactory
                .select(promiseShareKey.encUserId)
                .from(promiseShareKey)
                .where(promiseShareKey.promiseId.in(promiseIdSubQuery))
                .fetch();
    }
}
