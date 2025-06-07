package timetogeter.context.schedule.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.schedule.domain.entity.Schedule;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String > {

    List<Schedule> findAllByIdIn(List<String> scheduleIds);

    List<Schedule> findAllByGroupIdAndIdIn(String groupId, List<String> strings);
}
