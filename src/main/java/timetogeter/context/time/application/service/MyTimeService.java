package timetogeter.context.time.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.time.application.dto.UserTimeSlotDTO;
import timetogeter.context.time.application.dto.request.UserTimeSlotReqDTO;
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

    @Transactional
    public void updateUserTime(String userId, String promiseId, UserTimeSlotReqDTO reqDTO) {
        // TODO: PromiseCheck는 없어도 될 것 같음
        // TODO: 날짜 범위 내 있는지 확인 (예외처리) 고민

        for(UserTimeSlotDTO dateTime : reqDTO.dateTime()){ // find or create 로 분리할지 고민
            PromiseDate date = promiseDateRepository
                    .findByPromiseIdAndDate(promiseId, dateTime.date())
                    .orElseGet(() -> promiseDateRepository.save(
                            new PromiseDate(dateTime.day(), dateTime.date(), promiseId)
                    ));

            for(LocalTime time : dateTime.times()){
                PromiseTime promiseTime = new PromiseTime(date.getDateId(), time, userId);
                promiseTimeRepository.save(promiseTime);
            }

        }
    }
}
