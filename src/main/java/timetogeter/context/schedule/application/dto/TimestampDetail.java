package timetogeter.context.schedule.application.dto;

import java.time.LocalDate;

public record TimestampDetail(LocalDate date,
                              String timestamp) {
}
