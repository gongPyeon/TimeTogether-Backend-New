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

        // userId가 null이면 빈 리스트 반환
        if (userId == null) {
            return List.of();
        }

        List<PlaceRatingDTO> result = queryFactory
                .select(Projections.constructor(PlaceRatingDTO.class,
                        u.placeBoardId,
                        u.userId,
                        u.rating
                ))
                .from(u)
                .where(u.userId.eq(userId))
                .fetch();

        return result != null ? result : List.of();
    }

    @Override
    public List<PlaceRatingDTO> findAllRatings() {
        QUserBoard u = QUserBoard.userBoard;

        List<PlaceRatingDTO> result = queryFactory
                .select(Projections.constructor(PlaceRatingDTO.class,
                        u.placeBoardId,
                        u.userId,
                        u.rating
                ))
                .from(u)
                .fetch();

        return result != null ? result : List.of();
    }
}