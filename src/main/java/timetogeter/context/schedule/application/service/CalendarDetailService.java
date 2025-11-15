package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.place.domain.entity.PlaceBoard;
import timetogeter.context.place.domain.repository.PlaceBoardRepository;
import timetogeter.context.schedule.application.dto.request.CalendarCreateRequest1;
import timetogeter.context.schedule.application.dto.request.CalendarCreateRequest2;
import timetogeter.context.schedule.application.dto.request.CalendarRewriteRequest1;
import timetogeter.context.schedule.application.dto.response.CalendarCreateResponse1;
import timetogeter.context.schedule.application.dto.response.CalendarCreateResponse2;
import timetogeter.context.schedule.application.dto.response.CalendarRewriteResponse1;
import timetogeter.context.promise.domain.entity.PromiseShareKey;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.entity.TimeStamp;
import timetogeter.context.promise.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;
import timetogeter.context.schedule.domain.repository.TimeStampRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CalendarDetailService {

    private final PromiseShareKeyRepository promiseShareKeyRepository;
    private final ScheduleRepository scheduleRepository;
    private final PlaceBoardRepository placeBoardRepository;
    private final TimeStampRepository timeStampRepository;

//======================
// 캘린더 - 일정등록 (Step1)
//======================

    //캘린더 - 일정등록 - Step1 - 메인 서비스 메소드
    @Transactional
    public CalendarCreateResponse1 createCalendar1(CalendarCreateRequest1 request, String userId) {
        // Place 생성 및 저장
        PlaceBoard place = PlaceBoard.of(request.placeName(), request.placeAddr(), true, request.placeInfo());
        placeBoardRepository.save(place);

        // Schedule 생성 및 저장 (scheduleId는 UUID로 생성)
        String scheduleId = UUID.randomUUID().toString();
        Schedule schedule = Schedule.of(
                scheduleId,
                request.title(),
                request.content(),
                request.purpose(),
                place.getPlaceBoardId(),
                null// groupId (현재 null 처리)

        );
        scheduleRepository.save(schedule);

        return new CalendarCreateResponse1(
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent()
        );
    }

//======================
// 캘린더 - 개인 일정등록 (Step2)
//======================

    //캘린더 - 개인 일정등록 - Step2 - 메인 서비스 메소드
    @Transactional
    public CalendarCreateResponse2 createCalendar2(CalendarCreateRequest2 request, String userId) {
        //TimeStamp 생성 및 저장
        TimeStamp timeStamp = TimeStamp.of(
                request.encStartTimeAndEndTime(),
                request.timeStampInfo(),
                userId
        );
        timeStampRepository.save(timeStamp);

        // 응답 반환
        return new CalendarCreateResponse2("개인 일정을 저장했습니다.");
    }

//======================
// 캘린더 - 일정수정 (Step1)
//======================

    //캘린더 - 일정수정 - Step1 - 메인 서비스 메소드
    @Transactional
    public CalendarRewriteResponse1 rewriteCalendar1(CalendarRewriteRequest1 request) {
        // 1. encPromiseKey + encUserId 로 scheduleId 찾기
        PromiseShareKey key = promiseShareKeyRepository.findByEncPromiseKeyAndEncUserId(
                request.encPromiseKey(), request.encUserId()
        ).orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다.")); //TODO: 예외처리 수정예정

        String scheduleId = key.getScheduleId();

        // 2. scheduleId 로 Schedule 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        // 3. 장소 저장
        // Place 생성 및 저장
        PlaceBoard place = PlaceBoard.of(request.placeName(), request.placeAddr(), true, request.placeInfo());
        placeBoardRepository.save(place);

        // 3. Schedule 값 수정
        schedule.update(
                request.title(),
                request.content(),
                request.purpose(),
                place.getPlaceBoardId(),
                null
        );

        // 5. 응답 반환
        return new CalendarRewriteResponse1(
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getPurpose(),
                place.getPlaceName(),
                place.getPlaceAddr()

        );

    }

}
