package timetogeter.context.schedule.application.dto.request;

import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDate;
import java.util.List;

public record ScheduleConfirmReqDTO (String promiseId,
                                     String scheduleId,
                                     String encTimeStamp,
                                     LocalDate timeStampInfo,
                                     int placeId,
                                     String title,
                                     String purpose,
                                     List<String> userList) {}
