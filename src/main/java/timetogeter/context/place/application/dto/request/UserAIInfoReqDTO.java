package timetogeter.context.place.application.dto.request;

import java.util.List;
public record UserAIInfoReqDTO(String pseudoId,
                               String purpose,
                               double latitude,
                               double longitude){}
// 위도, 경도