package timetogeter.context.place.application.dto.request;

import java.util.List;
public record UserAIInfoReqDTO(double latitude,
                               double longitude,
                               List<String> preferredCategories){}
// 위도, 경도, 선호 카테고리