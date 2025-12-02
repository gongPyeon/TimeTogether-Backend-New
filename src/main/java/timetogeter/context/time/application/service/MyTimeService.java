package timetogeter.context.time.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.schedule.application.service.ScheduleQueryService;
import timetogeter.context.time.application.dto.UserTimeSlotDTO;
import timetogeter.context.time.application.dto.request.UserTimeSlotReqDTO;
import timetogeter.context.time.application.dto.response.UserScheduleResDTO;
import timetogeter.context.time.domain.entity.PromiseDate;
import timetogeter.context.time.domain.entity.PromiseTime;
import timetogeter.context.time.domain.repository.PromiseDateRepository;
import timetogeter.context.time.domain.repository.PromiseTimeRepository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyTimeService {

    private final PromiseDateRepository promiseDateRepository;
    private final PromiseTimeRepository promiseTimeRepository;
    private final ScheduleQueryService scheduleQueryService;

    @Transactional
    public void updateUserTime(String userId, String promiseId, UserTimeSlotReqDTO reqDTO) {
        promiseTimeRepository.deleteByPromiseIdAndUserId(promiseId, userId);
        for(UserTimeSlotDTO dateTime : reqDTO.dateTime()){ // find or create 로 분리할지 고민
            PromiseDate date = promiseDateRepository
                    .findByPromiseIdAndDate(promiseId, dateTime.date())
                    .orElseGet(() -> promiseDateRepository.save(
                            new PromiseDate(dateTime.day(), dateTime.date(), promiseId)
                    ));

            for(LocalTime time : dateTime.times()){
                PromiseTime promiseTime = new PromiseTime(date.getDateId(), time, userId, promiseId);
                promiseTimeRepository.save(promiseTime);
            }

        }
    }

    public UserScheduleResDTO loadUserSchedule(String userId, String promiseId) {
        List<String> scheduleIds = scheduleQueryService.getScheduleIds(userId, promiseId);

        return new UserScheduleResDTO(scheduleIds);
    }
}
