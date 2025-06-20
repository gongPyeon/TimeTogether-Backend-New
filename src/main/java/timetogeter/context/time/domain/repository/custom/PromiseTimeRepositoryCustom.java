package timetogeter.context.time.domain.repository.custom;

import org.springframework.data.repository.query.Param;
import timetogeter.context.time.application.dto.DailyTimeDTO;

import java.util.List;

public interface PromiseTimeRepositoryCustom {
    List<DailyTimeDTO> findAllWithDailyTimesByPromiseId(String promiseId);
}
