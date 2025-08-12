package timetogeter.context.place.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import timetogeter.context.place.application.dto.PlaceRatingDTO;
import timetogeter.context.place.domain.entity.QUserBoard;
import timetogeter.context.place.domain.repository.custom.PlaceBoardRepositoryCustom;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceBoardRepositoryImpl implements PlaceBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceRatingDTO> findAllRatingsByUserId(String userId) {
        QUserBoard u = QUserBoard.userBoard;

        return queryFactory
                .select(Projections.constructor(PlaceRatingDTO.class,
                        u.placeBoardId,
                        u.userId,
                        u.rating
                ))
                .from(u)
                .where(u.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<PlaceRatingDTO> findAllRatings() {
        QUserBoard u = QUserBoard.userBoard;

        return queryFactory
                .select(Projections.constructor(PlaceRatingDTO.class,
                        u.placeBoardId,
                        u.userId,
                        u.rating
                ))
                .from(u)
                .fetch();
    }
}