package timetogeter.context.time.application.dto.request;

import timetogeter.context.time.application.dto.UserTimeSlotDTO;

import java.util.List;

public record UserTimeSlotReqDTO(List<UserTimeSlotDTO> dateTime) {
}
