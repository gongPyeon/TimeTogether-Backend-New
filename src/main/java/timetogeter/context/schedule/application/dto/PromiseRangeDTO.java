package timetogeter.context.schedule.application.dto;

import java.time.LocalDate;

public record PromiseRangeDTO(LocalDate startDate,
                              LocalDate endDate) {}
