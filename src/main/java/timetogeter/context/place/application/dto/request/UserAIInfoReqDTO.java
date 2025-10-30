package timetogeter.context.place.application.dto.request;

import java.util.List;
public record UserAIInfoReqDTO(String pseudoId,
                               double latitude,
                               double longitude){}
// 위도, 경도