package timetogeter.context.schedule.application.dto.response;

import timetogeter.context.promise.domain.vo.PromiseType;

public record PromiseResDTO(String scheduleId,
                            String title,
                            String purpose,
                            boolean isRated) {
}
