package timetogeter.context.schedule.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.custom.ScheduleRepositoryCustom;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String >, ScheduleRepositoryCustom {

    List<Schedule> findAllByScheduleIdIn(List<String> scheduleIds);

    List<Schedule> findAllByGroupIdAndScheduleIdIn(String groupId, List<String> strings);
}
