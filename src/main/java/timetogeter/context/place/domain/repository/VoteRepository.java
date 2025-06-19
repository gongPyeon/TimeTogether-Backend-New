package timetogeter.context.place.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timetogeter.context.place.domain.entity.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, String> {
    Optional<Vote> findByPlaceIdAndUserId(int placeId, String userId);
}
