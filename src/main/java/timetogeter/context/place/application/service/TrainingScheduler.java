package timetogeter.context.place.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingScheduler {

    // @Scheduled(fixedRate = 600000) // 10분마다 실행 (단위: ms)
    public void sendTrainingData() {

    }
}
