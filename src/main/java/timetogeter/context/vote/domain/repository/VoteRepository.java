package timetogeter.context.vote.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timetogeter.context.vote.domain.entity.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, String> {
    Optional<Vote> findByPlaceIdAndUserId(int placeId, String userId);
}
