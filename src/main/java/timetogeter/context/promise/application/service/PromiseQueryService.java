package timetogeter.context.promise.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.promise.application.dto.TimeRange;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.context.promise.exception.PromiseNotFoundException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Service
@RequiredArgsConstructor
public class PromiseQueryService { // promise 테이블에 대한 단순 조회 서비스

    private final PromiseRepository promiseRepository;
    public TimeRange getTimeRange(String promiseId) {
        Promise promise = get(promiseId);
        if(promise.getStartDate() == null || promise.getEndDate() == null)
            throw new PromiseNotFoundException(BaseErrorCode.PROMISE_TIME_RANGE_NOT_FOUND, "[ERROR] " + promiseId + "에 해당하는 약속 (시간)범위가 존재하지 않습니다.");

        return new TimeRange(promise.getStartDate(), promise.getEndDate());
    }

    private Promise get(String promiseId){
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(() -> new PromiseNotFoundException(BaseErrorCode.PROMISE_NOT_FOUND, "[ERROR] " + promiseId + "에 해당하는 약속이 존재하지 않습니다."));
        return promise;
    }
}
