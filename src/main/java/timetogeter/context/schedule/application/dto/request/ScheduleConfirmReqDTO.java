package timetogeter.context.schedule.application.dto.request;

import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDate;

public record ScheduleConfirmReqDTO (String promiseId,
                                     String scheduleId,
                                     String encTimeStamp,
                                     LocalDate timeStampInfo,
                                     int placeId,
                                     String title,
                                     String purpose) {}
