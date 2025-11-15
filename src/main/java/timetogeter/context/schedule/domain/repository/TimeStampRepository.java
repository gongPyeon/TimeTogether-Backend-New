package timetogeter.context.schedule.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import timetogeter.context.schedule.domain.entity.TimeStamp;
import timetogeter.context.schedule.domain.repository.custom.TimeStampRepositoryCustom;

import java.time.LocalDate;
import java.util.List;

public interface TimeStampRepository extends JpaRepository<TimeStamp, Long>, TimeStampRepositoryCustom {
}
