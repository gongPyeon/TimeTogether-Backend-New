package timetogeter.context.time.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UserTimeBoardResDTO(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  @JsonFormat(pattern = "HH:mm:ss") LocalTime time,
                                  List<String> availableUsers,
                                  List<String> unavailableUsers) {}
