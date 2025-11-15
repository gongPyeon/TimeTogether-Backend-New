package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.schedule.application.dto.request.CalendarViewRequest1;
import timetogeter.context.schedule.application.dto.request.CalendarViewRequest2;
import timetogeter.context.schedule.application.dto.response.CalendarViewResponse1;
import timetogeter.context.schedule.application.dto.response.CalendarViewResponse2;
import timetogeter.context.place.domain.entity.PlaceBoard;
import timetogeter.context.place.domain.repository.PlaceBoardRepository;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;
import timetogeter.context.schedule.domain.repository.TimeStampRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CalendarViewService {

    private final ScheduleRepository scheduleRepository;
    private final PlaceBoardRepository placeBoardRepository;
    private final TimeStampRepository timeStampRepository;

//======================
// 캘린더 - 캘린더 메인, 캘린더 일정 확인 (Step1,2)
//======================

    //캘린더 - 캘린더 메인, 캘린더 일정 확인 - Step1 - 메인 서비스 메소드
    public CalendarViewResponse1 viewCalendar1(CalendarViewRequest1 request, String userId) {
        List<java.time.LocalDate> timeStampInfoList = request.timeStampInfoList();

        // timeStampInfo와 userId로 encTimeStamp 조회
        List<String> encTimeStampList = timeStampRepository.findTimeStampsByUserIdAndTimeStamp(userId, timeStampInfoList);

        return new CalendarViewResponse1(encTimeStampList);
    }

    //캘린더 - 캘린더 메인, 캘린더 일정 확인 - Step2 - 메인 서비스 메소드
    public List<CalendarViewResponse2> viewCalendar2(CalendarViewRequest2 request) {
        List<String> scheduleIdList = request.scheduleIdList();

        // scheduleId에 해당하는 Schedule 객체들 조회
        // ----------------------------
        // 1. 쿼리 시간 측정
        // ----------------------------
        long queryStart = System.currentTimeMillis();
        List<Schedule> schedules = scheduleRepository.findByIdIn(scheduleIdList);
        long queryEnd = System.currentTimeMillis();
        log.info("scheduleList find query time {} ms", queryEnd - queryStart);
        
        // Schedule -> CalendarViewResponse2 로 매핑 (장소 정보 포함)
        // ----------------------------
        // 2. DTO 매핑 시간 측정
        // ----------------------------
        long mappingStart = System.currentTimeMillis();
        List<CalendarViewResponse2> response = schedules.stream()
                .map(schedule -> {
                    // PlaceBoard 조회
                    PlaceBoard place = placeBoardRepository.findById(schedule.getPlaceId())
                            .orElse(null);
                    
                    return new CalendarViewResponse2(
                            schedule.getScheduleId(),
                            schedule.getTitle(),
                            schedule.getContent(),
                            schedule.getPurpose(),
                            place != null ? place.getPlaceName() : null,
                            place != null ? place.getPlaceAddr() : null,
                            place != null ? place.getPlaceInfo() : null
                    );
                })
                .collect(Collectors.toList());
        long mappingEnd = System.currentTimeMillis();
        log.info("DTO mapping time {} ms", mappingEnd - mappingStart);

        return response;
    }

}
