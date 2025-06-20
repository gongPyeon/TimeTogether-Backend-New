package timetogeter.context.time.application.dto;

import timetogeter.context.time.domain.vo.WeekDay;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UserTimeSlotDTO(LocalDate date,
                              WeekDay day,
                              List<LocalTime> times) {}