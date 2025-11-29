package timetogeter.context.promise.domain.repository.custom;

import java.util.List;

public interface PromiseShareRepositoryCustom {
    List<String> findNamesByScheduleId(String scheduleId);
}
