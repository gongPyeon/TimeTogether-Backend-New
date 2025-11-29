package timetogeter.context.schedule.application.dto.response;

import timetogeter.context.schedule.application.dto.TimestampDetail;

import java.util.List;

public record TimestampResDTO(List<TimestampDetail> timeStamps) {
}
