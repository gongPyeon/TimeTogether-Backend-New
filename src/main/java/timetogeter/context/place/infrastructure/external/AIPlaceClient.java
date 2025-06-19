package timetogeter.context.place.infrastructure.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import timetogeter.context.place.application.dto.PlaceDTO;
import timetogeter.context.place.application.dto.request.AIReqDTO;
import timetogeter.context.place.application.dto.request.PlaceRegisterDTO;
import timetogeter.global.security.util.api.ApiService;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AIPlaceClient {
    private final ApiService apiService;

    @Value("${ai.api.url}")
    private String aiApiUrl;

    // TODO: PlaceRegisterDTO 위치 AI가 최대 20개만 반환(말하기)
    public List<PlaceRegisterDTO> requestAIRecommendation(AIReqDTO aiReqDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AIReqDTO> entity = new HttpEntity<>(aiReqDTO, headers);
        ResponseEntity<List<PlaceRegisterDTO>> response = apiService.send(aiApiUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

}
