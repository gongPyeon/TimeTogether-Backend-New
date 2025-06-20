package timetogeter.context.schedule.application.dto.request;

import timetogeter.context.promise.domain.vo.PromiseType;

public record ScheduleConfirmReqDTO (String scheduleId,
                                     int placeId,
                                     String title,
                                     String purpose) {}
