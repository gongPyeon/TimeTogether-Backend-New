package timetogeter.global.security.util.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Map> send(String url, HttpEntity<?> request){
        return restTemplate.postForEntity(url, request, Map.class);
    }

    public ResponseEntity<Map> send(String url, HttpMethod httpMethod, HttpEntity<?> request){
        return restTemplate.exchange(url, httpMethod, request, Map.class);
    }
}
