package timetogeter.context.time.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TimeRangeDTO(
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate
) {}
