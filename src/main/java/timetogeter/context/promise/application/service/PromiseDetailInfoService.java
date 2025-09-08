package timetogeter.context.promise.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.promise.application.dto.request.detail.PromiseView3Request;
import timetogeter.context.promise.application.dto.request.detail.PromiseView4Request;
import timetogeter.context.promise.application.dto.request.detail.Promiseview2Request;
import timetogeter.context.promise.application.dto.response.detail.PromiseView1Response;
import timetogeter.context.promise.application.dto.response.detail.PromiseView2Response;
import timetogeter.context.promise.application.dto.response.detail.PromiseView3Response;
import timetogeter.context.promise.application.dto.response.detail.PromiseView4Response;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.repository.PromiseProxyUserRepository;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.context.promise.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PromiseDetailInfoService {

    private final PromiseProxyUserRepository promiseProxyUserRepository;
    private final PromiseRepository promiseRepository;
    private final PromiseShareKeyRepository promiseShareKeyRepository;
    private final ScheduleRepository scheduleRepository;

    // 디테일 확인 - 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 Step1 - 메인 메소드
    public List<PromiseView1Response> getEncPromiseIdList(String userId) {
        List<String> encPromiseIds = promiseProxyUserRepository.findPromiseIdsByUserId(userId);

        return encPromiseIds.stream()
                .map(encPromiseId -> new PromiseView1Response(encPromiseId))
                .collect(Collectors.toList());
    }

    // 디테일 확인 - 사용자가 속한 그룹 내 약속을 (정하는중) 로 구분지어 보여주는 화면 Step2 - 메인 메소드
    public List<PromiseView2Response> getPromiseInfoList(String userId, Promiseview2Request request) {
        //1. request내 각 promiseId들에 대해 PromiseRepository객체를 반환
        List<String> promiseIdList = request.promiseIdList();
        List<Promise> promises = promiseRepository.findByPromiseIdIn(promiseIdList);


        //2. request내 각 promiseId들에 대해 PromiseCheck내의 insConfirmed를 반환
        return promises.stream()
                .map(promise -> new PromiseView2Response(
                        false,
                        promise.getPromiseId(),
                        promise.getTitle(),
                        promise.getType(),
                        promise.getStartDate().toString(),
                        promise.getEndDate().toString(),
                        promise.getManagerId(),
                        promise.getPromiseImg()
                ))
                .collect(Collectors.toList());
    }

    // 디테일 확인 - 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 Step3 - 메인 메소드
    public List<PromiseView3Response> getScheduleIdList(String userId, PromiseView3Request request) {
        List<String> encPromiseKeyList = request.encPromiseKeyList();

        // encPromiseKey에 해당하는 scheduleId 조회
        List<String> scheduleIds = promiseShareKeyRepository.findScheduleIdsByEncPromiseKeyList(encPromiseKeyList);

        // 응답으로 변환
        return scheduleIds.stream()
                .map(PromiseView3Response::new)
                .collect(Collectors.toList());
    }

    // 디테일 확인 - 사용자가 속한 그룹 내 약속을 (확정완료) 로 구분지어 보여주는 화면 Step4 - 메인 메소드
    public List<PromiseView4Response> getScheduleInfoList(String userId, PromiseView4Request request) {
        //request내 각 scheduleId들에 대해 Schedule객체를 반환
        List<String> scheduleIdList = request.sheduleIdList();
        List<Schedule> schedules = scheduleRepository.findByScheduleIdIn(scheduleIdList);

        return schedules.stream()
                .map(schedule -> new PromiseView4Response(
                        true, //Schedule 엔티티는 다 확정된 일정들
                        schedule.getScheduleId(),
                        schedule.getScheduleId(),
                        schedule.getTitle(),
                        schedule.getPurpose(),
                        schedule.getPlaceId(),
                        schedule.getGroupId()
                ))
                .collect(Collectors.toList());
    }
}
