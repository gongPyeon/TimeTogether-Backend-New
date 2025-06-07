package timetogeter.context.schedule.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<Schedule, String> {
}