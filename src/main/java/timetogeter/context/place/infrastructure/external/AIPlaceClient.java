package timetogeter.context.place.infrastructure.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import timetogeter.context.place.application.dto.AIResDTO;
import timetogeter.context.place.application.dto.PlaceRatingDTO;
import timetogeter.context.place.application.dto.SimpleAIResDTO;
import timetogeter.context.place.application.dto.request.AIReqDTO;
import timetogeter.context.place.application.dto.response.PlaceRegisterResDTO;
import timetogeter.global.security.util.api.ApiService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIPlaceClient {
    private final ApiService apiService;

    @Value("${ai.api.url}")
    private String aiApiUrl;

    public List<PlaceRegisterResDTO> requestAIRecommendation(AIReqDTO aiReqDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AIReqDTO> entity = new HttpEntity<>(aiReqDTO, headers);

        String url = aiApiUrl + "/place/recommend";
        ResponseEntity<AIResDTO> response = apiService.send(url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});

        AIResDTO aiResDTO = response.getBody();
        if (aiResDTO != null) {
            return aiResDTO.result();
        }

        return List.of();
    }

    public SimpleAIResDTO requestAITraining(List<PlaceRatingDTO> history) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<PlaceRatingDTO>> entity = new HttpEntity<>(history, headers);
        String url = aiApiUrl + "/place/train";
        ResponseEntity<SimpleAIResDTO> response = apiService.send(url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

}
