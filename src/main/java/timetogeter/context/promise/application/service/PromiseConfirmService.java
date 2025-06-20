package timetogeter.context.promise.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.place.application.dto.PlaceRegisterDTO;
import timetogeter.context.place.exception.VoteFailException;
import timetogeter.context.promise.application.dto.response.PromiseRegisterDTO;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.entity.PromiseCheck;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.context.promise.exception.PromiseNotFoundException;
import timetogeter.context.time.application.service.TimeConfirmService;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromiseConfirmService {

    private final PromiseRepository promiseRepository;
    private final TimeConfirmService timeConfirmService;


    public boolean confirmedPlaceManager(String userId, String promiseId){
        String managerId = promiseRepository.findMangerById(promiseId)
                .orElseThrow(() -> new PromiseNotFoundException(BaseErrorCode.PROMISE_NOT_FOUND,
                "[ERROR] " + promiseId + "에 해당하는 약속이 존재하지 않습니다."));

        return managerId.equals(userId);
    }

    public PromiseRegisterDTO confirmedSchedule(String promiseId, PlaceRegisterDTO place) {
        Optional<PromiseCheck> confirmedTime = timeConfirmService.isConfirmedTime(promiseId);
        if(!confirmedTime.isPresent()) return new PromiseRegisterDTO();

        Promise promise = get(promiseId);
        PromiseCheck time = confirmedTime.get();

        return new PromiseRegisterDTO(time.getDateTime(),
                promise.getTitle(), promise.getType(),
                place.placeName(), place.placeUrl());
    }

    public void checkTotalVote(String promiseId, int voteNum) {
        Promise promise = get(promiseId);
        if(voteNum >= promise.getNum())
            throw new VoteFailException(BaseErrorCode.VOTE_NUM_MAX, "[ERROR] 투표는 약속 참여원 수만큼 가능합니다.");
    }

    private Promise get(String promiseId){
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseNotFoundException(BaseErrorCode.PROMISE_NOT_FOUND, "[ERROR] " + promiseId + "에 해당하는 약속이 존재하지 않습니다."));
        return promise;
    }

}
