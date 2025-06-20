package timetogeter.context.schedule.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.custom.ScheduleRepositoryCustom;

import java.util.List;

@Repository
public interface ScheduleRepository  extends JpaRepository<Schedule, String> , ScheduleRepositoryCustom{
    @Query(value = "SELECT * FROM schedule WHERE schedule_id IN (:scheduleIdList)", nativeQuery = true)
    List<Schedule> findByScheduleIdIn(@Param("scheduleIdList") List<String> scheduleIdList);

    List<Schedule> findAllByScheduleIdIn(List<String> scheduleIds);

    List<Schedule> findAllByGroupIdAndScheduleIdIn(String groupId, List<String> strings);

    @Query(value = "SELECT * FROM schedule WHERE schedule_id IN (:scheduleIdList)", nativeQuery = true)
    List<Schedule> findByIdIn(@Param("scheduleIdList") List<String> scheduleIdList);

}
