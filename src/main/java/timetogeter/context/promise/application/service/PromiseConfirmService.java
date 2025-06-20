package timetogeter.context.promise.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.place.exception.VoteFailException;
import timetogeter.context.promise.application.dto.response.PromiseRegisterDTO;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.entity.PromiseCheck;
import timetogeter.context.promise.domain.repository.PromiseCheckRepository;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.context.promise.exception.PromiseNotFoundException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Service
@RequiredArgsConstructor
public class PromiseConfirmService {

    private final PromiseRepository promiseRepository;
    private final PromiseCheckRepository promiseCheckRepository;


    public boolean confirmedPromiseManager(String userId, String promiseId){
        String managerId = promiseRepository.findMangerById(promiseId)
                .orElseThrow(() -> new PromiseNotFoundException(BaseErrorCode.PROMISE_NOT_FOUND,
                "[ERROR] " + promiseId + "에 해당하는 약속이 존재하지 않습니다."));

        return managerId.equals(userId);
    }

    public PromiseRegisterDTO confirmedSchedule(String promiseId, int placeId) {
        Promise promise = get(promiseId);
        if(!promise.getDateTimeCheck()) return new PromiseRegisterDTO();

        PromiseCheck promiseCheck = getPromiseCheck(promiseId);

        return new PromiseRegisterDTO(promiseCheck.getDateTime(),
                promise.getTitle(), promise.getType(),
                promiseCheck.getPlaceId());
    }

    public PromiseRegisterDTO confirmedSchedule(String promiseId, String DateTime) {
        Promise promise = get(promiseId);
        if(!promise.getPlaceCheck()) return new PromiseRegisterDTO();

        PromiseCheck promiseCheck = getPromiseCheck(promiseId);

        return new PromiseRegisterDTO(promiseCheck.getDateTime(),
                promise.getTitle(), promise.getType(),
                promiseCheck.getPlaceId());
    }

    // TODO: 두개의 리포지토리 업데이트?
    @Transactional
    public void confirmPromisePlace(String promiseId, int placeId){
        Promise promise = get(promiseId);
        promise.confirmPlaceCheck();
        promiseRepository.save(promise);

        PromiseCheck promiseCheck = new PromiseCheck(placeId);
        promiseCheckRepository.save(promiseCheck);
    }

    @Transactional
    public void confirmPromiseDateTime(String promiseId, String DateTime){
        Promise promise = get(promiseId);
        promise.confirmDateTimeCheck();
        promiseRepository.save(promise);

        PromiseCheck promiseCheck = new PromiseCheck(DateTime);
        promiseCheckRepository.save(promiseCheck);
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

    private PromiseCheck getPromiseCheck(String promiseId){
        PromiseCheck promiseCheck = promiseCheckRepository.findByPromiseId(promiseId)
                .orElseThrow(() -> new PromiseNotFoundException(BaseErrorCode.PROMISE_CHECK_NOT_FOUND, "[ERROR] " + promiseId + "에 해당하는 약속체크가 존재하지 않습니다."));
        return promiseCheck;
    }

}
