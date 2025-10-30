package timetogeter.context.promise.infrastructure.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import timetogeter.context.place.domain.entity.QPromisePlace;
import timetogeter.context.place.domain.entity.QVote;
import timetogeter.context.promise.domain.entity.QPromise;
import timetogeter.context.promise.domain.repository.custom.PromiseRepositoryCustom;
import timetogeter.context.time.domain.entity.QPromiseDate;
import timetogeter.context.time.domain.entity.QPromiseTime;

@Repository
@RequiredArgsConstructor
public class PromiseRepositoryImpl implements PromiseRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void deletePromiseWithRelatedData(String promiseId) {
        long voteCount = queryFactory
                .delete(QVote.vote)
                .where(QVote.vote.placeId.in(
                        JPAExpressions
                                .select(QPromisePlace.promisePlace.placeId)
                                .from(QPromisePlace.promisePlace)
                                .where(QPromisePlace.promisePlace.promiseId.eq(promiseId))
                ))
                .execute();

        long placCount = queryFactory
                .delete(QPromisePlace.promisePlace)
                .where(QPromisePlace.promisePlace.promiseId.eq(promiseId))
                .execute();

        long timeCount = queryFactory
                .delete(QPromiseTime.promiseTime)
                .where(QPromiseTime.promiseTime.dateId.in(
                        JPAExpressions
                                .select(QPromiseDate.promiseDate.dateId)
                                .from(QPromiseDate.promiseDate)
                                .where(QPromiseDate.promiseDate.promiseId.eq(promiseId))
                ))
                .execute();

        long dateCount = queryFactory
                .delete(QPromiseDate.promiseDate)
                .where(QPromiseDate.promiseDate.promiseId.eq(promiseId))
                .execute();

        long promiseCount = queryFactory
                .delete(QPromise.promise)
                .where(QPromise.promise.promiseId.eq(promiseId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }
}
