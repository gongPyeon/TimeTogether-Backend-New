package timetogeter.context.schedule.application.dto.request;

import java.time.LocalDate;
import java.util.List;

public record TimestampReqDTO (List<LocalDate> dates) {}
