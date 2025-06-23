package timetogeter.context.place.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import timetogeter.context.place.application.dto.PlaceRatingDTO;
import timetogeter.context.place.application.dto.SimpleAIResDTO;
import timetogeter.context.place.domain.repository.PlaceBoardRepository;
import timetogeter.context.place.infrastructure.external.AIPlaceClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrainingScheduler {

    private final AIPlaceClient aiPlaceClient;
    private final PlaceBoardRepository placeBoardRepository;

    // @Scheduled(fixedRate = 600000) // 10분마다 실행 (단위: ms)
    public void sendTrainingData() {
        List<PlaceRatingDTO> history = placeBoardRepository.findAllRatings();
        SimpleAIResDTO resDTO = aiPlaceClient.requestAITraining(history);
        log.info("code:{}, message:{}", resDTO.code(), resDTO.message());
    }
}
