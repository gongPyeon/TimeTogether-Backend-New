package timetogeter.context.time.domain.repository.custom;

import org.springframework.data.repository.query.Param;
import timetogeter.context.time.application.dto.DailyTimeDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PromiseTimeRepositoryCustom {
    List<DailyTimeDTO> findAllWithDailyTimesByPromiseId(String promiseId);
    List<String> findUsersAtTime(String promiseId, LocalDate date, LocalTime time);
}
