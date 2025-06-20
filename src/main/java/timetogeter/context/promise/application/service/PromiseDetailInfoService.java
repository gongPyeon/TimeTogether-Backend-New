package timetogeter.context.promise.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.promise.application.dto.request.detail.OverallPromiseViewRequest3;
import timetogeter.context.promise.application.dto.request.detail.OverallPromiseViewRequest4;
import timetogeter.context.promise.application.dto.request.detail.OverallPromiseviewRequest2;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse1;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse2;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse3;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse4;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.entity.PromiseCheck;
import timetogeter.context.promise.domain.repository.PromiseProxyUserRepository;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;

import java.util.List;
import java.util.Map;
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

    // 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 Step1 - 메인 메소드
    public List<OverallPromiseViewResponse1> getEncPromiseIdList(String userId) {
        List<String> encPromiseIds = promiseProxyUserRepository.findPromiseIdsByUserId(userId);

        return encPromiseIds.stream()
                .map(encPromiseId -> new OverallPromiseViewResponse1(encPromiseId))
                .collect(Collectors.toList());
    }

    // 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 Step2 - 메인 메소드
    public List<OverallPromiseViewResponse2> getPromiseInfoList(String userId, OverallPromiseviewRequest2 request) {
        //1. request내 각 promiseId들에 대해 PromiseRepository객체를 반환
        List<String> promiseIdList = request.promiseIdList();
        List<Promise> promises = promiseRepository.findByPromiseIdIn(promiseIdList);


        //2. request내 각 promiseId들에 대해 PromiseCheck내의 insConfirmed를 반환 //TODO: PromiseCheck에 대한 확정 로직이 먼저 만들어져야 함.
        return promises.stream()
                .map(promise -> new OverallPromiseViewResponse2(
                        false,
                        promise.getPromiseId(),
                        promise.getTitle(),
                        promise.getType().name(),
                        promise.getStartDate().toString(),
                        promise.getEndDate().toString(),
                        promise.getManagerId(),
                        promise.getPromiseImg()
                ))
                .collect(Collectors.toList());
    }

    // 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 Step3 - 메인 메소드
    public List<OverallPromiseViewResponse3> getScheduleIdList(String userId, OverallPromiseViewRequest3 request) {
        List<String> encPromiseKeyList = request.encPromiseKeyList();

        // encPromiseKey에 해당하는 scheduleId 조회
        List<String> scheduleIds = promiseShareKeyRepository.findScheduleIdsByEncPromiseKeyList(encPromiseKeyList);

        // 응답으로 변환
        return scheduleIds.stream()
                .map(OverallPromiseViewResponse3::new)
                .collect(Collectors.toList());
    }

    // 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 Step4 - 메인 메소드
    public List<OverallPromiseViewResponse4> getScheduleInfoList(String userId, OverallPromiseViewRequest4 request) {
        //request내 각 scheduleId들에 대해 Schedule객체를 반환
        List<String> scheduleIdList = request.sheduleIdList();
        List<Schedule> schedules = scheduleRepository.findByScheduleIdIn(scheduleIdList);

        return schedules.stream()
                .map(schedule -> new OverallPromiseViewResponse4(
                        true, //Schedule 엔티티는 다 확정된 일정들
                        schedule.getScheduleId(),
                        schedule.getScheduleId(),
                        schedule.getTitle(),
                        schedule.getType(),
                        schedule.getPlace(),
                        schedule.getPlaceUrl()
                ))
                .collect(Collectors.toList());
    }
}
