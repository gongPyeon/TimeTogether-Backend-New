package timetogeter.context.time.application.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TimeSlotReqDTO(LocalDate date,
                             LocalTime time,
                             List<String> userIds)
{}
