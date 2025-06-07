package timetogeter.context.schedule.application.dto;

import timetogeter.context.promise.domain.vo.PromiseType;

public record PromiseResDTO(String scheduleId,
                            String title,
                            String type) {
}
