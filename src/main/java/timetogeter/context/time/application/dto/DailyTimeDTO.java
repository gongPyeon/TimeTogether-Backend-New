package timetogeter.context.time.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record DailyTimeDTO(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                           List<TimeSlotDTO> times)
{}
