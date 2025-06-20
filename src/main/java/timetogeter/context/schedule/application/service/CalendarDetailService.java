package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.schedule.application.dto.request.CalendarCreateRequest1;
import timetogeter.context.schedule.application.dto.response.CalendarCreateResponse1;
import timetogeter.context.schedule.domain.entity.PromiseShareKey;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CalendarDetailService {

    private final PromiseShareKeyRepository promiseShareKeyRepository;
    private final ScheduleRepository scheduleRepository;

//======================
// 캘린더 - 일정등록 (Step1)
//======================

    //캘린더 - 일정등록 - Step1 - 메인 서비스 메소드
    @Transactional
    public CalendarCreateResponse1 createCalendar1(CalendarCreateRequest1 request) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(request.startDateTime(), formatter);
        LocalDateTime end = LocalDateTime.parse(request.endDateTime(), formatter);

        // Schedule 생성 및 저장 (of 메서드 사용)
        Schedule schedule = Schedule.of(
                request.title(),
                request.content(),
                request.type(),
                request.place(),
                request.placeUrl(),
                start,
                end,
                null, // groupId (현재 null 처리)
                null, // groupName (현재 null 처리)
                null  // promiseId (현재 null 처리)
        );
        scheduleRepository.save(schedule);

        // PromiseShareKey 저장
        PromiseShareKey key = PromiseShareKey.of(
                request.encPromiseKey(),
                request.encUserId(),
                schedule.getScheduleId()
        );
        promiseShareKeyRepository.save(key);

        // 응답 반환
        return new CalendarCreateResponse1(
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDateTime().format(formatter),
                schedule.getEndDateTime().format(formatter)
        );
    }

}
