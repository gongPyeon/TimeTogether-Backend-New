package timetogeter.context.vote.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import timetogeter.context.vote.domain.entity.Vote;
import timetogeter.context.vote.domain.repository.VoteRepository;
import timetogeter.context.vote.exception.VoteNotFoundException;
import timetogeter.context.vote.domain.repository.VoteRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Component
@RequiredArgsConstructor
public class VotingService { // TODO: 투표 관리 시스템

    private final VoteRepository voteRepository;
    @Transactional
    public void vote(String userId, int placeId) {
        voteRepository.save(new Vote(placeId, userId));
    }

    @Transactional
    public void cancelVote(String userId, int placeId) {
        Vote vote = voteRepository.findByPlaceIdAndUserId(placeId, userId)
                .orElseThrow(() -> new VoteNotFoundException(BaseErrorCode.PLACE_NOT_FOUND, "[ERROR] 사용자와 장소 아이디에 해당하는 투표를 찾을 수 없습니다."));

        voteRepository.delete(vote);
    }

    public boolean hasVotedBy(String userId, int placeId) {
        return voteRepository.findByPlaceIdAndUserId(placeId, userId).isPresent();
    }
}
