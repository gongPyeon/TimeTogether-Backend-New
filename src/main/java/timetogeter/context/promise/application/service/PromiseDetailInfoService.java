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
import timetogeter.context.place.domain.entity.PlaceBoard;
import timetogeter.context.place.domain.repository.PlaceBoardRepository;
import timetogeter.context.schedule.domain.entity.Schedule;
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
    private final PlaceBoardRepository placeBoardRepository;

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
        //+ 그냥 반환하는게 아니라, promise 테이블 내에서 groupId에 해당하는 promiseId와 겹치는거만 반환하도록 쿼리 추가
        List<String> promiseIdList = request.promiseIdList();
        String groupId = request.groupId();
        List<Promise> promises = promiseRepository.findByGroupIdAndPromiseIdIn(groupId, promiseIdList);


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
        List<String> promiseIdList = request.promiseIdList();
        List<String> encPromiseKeyList = promiseShareKeyRepository.findEncPromiseKeysByPromiseIdIn(promiseIdList);

        // encPromiseKey에 해당하는 scheduleId 조회
        List<String> scheduleIds = promiseShareKeyRepository.findScheduleIdsByEncPromiseKeyList(encPromiseKeyList);

        // 응답으로 변환
        return scheduleIds.stream()
                .distinct()
                .map(PromiseView3Response::new)
                .collect(Collectors.toList());
    }

    // 디테일 확인 - 사용자가 속한 그룹 내 약속을 (확정완료) 로 구분지어 보여주는 화면 Step4 - 메인 메소드
    public List<PromiseView4Response> getScheduleInfoList(String userId, PromiseView4Request request) {
        //request내 각 scheduleId들에 대해 Schedule객체를 반환
        List<String> scheduleIdList = request.scheduleIdList();
        List<Schedule> schedules = scheduleRepository.findByScheduleIdIn(scheduleIdList);

        //해당 schedules의 placeId 모두 수집
        List<Integer> placeIds = schedules.stream()
                .map(Schedule::getPlaceId)
                .distinct()
                .collect(Collectors.toList());

        //PlaceBoard 일괄 조회
        List<PlaceBoard> places = placeBoardRepository.findAllById(placeIds);

        //placeId로 매핑하여 빠른 조회를 위한 Map 생성
        Map<Integer, PlaceBoard> placeMap = places.stream()
                .collect(Collectors.toMap(PlaceBoard::getPlaceBoardId, place -> place));

        return schedules.stream()
                .map(schedule -> {
                    PlaceBoard place = placeMap.get(schedule.getPlaceId());
                    String confirmedDateTime = schedule.getScheduleId();
                    String content = schedule.getContent() != null ? schedule.getContent() : "";
                    String placeName = place != null ? place.getPlaceName() : "장소명 없음";
                    String placeInfo = place != null ? place.getPlaceInfo() : "";

                    return new PromiseView4Response(
                            true, //Schedule 엔티티는 다 확정된 일정들
                            schedule.getScheduleId(),
                            confirmedDateTime, //20251129Thhmm-20251129Thhmm 형식의 확정된 시간
                            schedule.getTitle(),
                            content,
                            schedule.getPurpose(),
                            placeName,
                            placeInfo,
                            schedule.getGroupId()
                    );
                })
                .collect(Collectors.toList());
    }
}
