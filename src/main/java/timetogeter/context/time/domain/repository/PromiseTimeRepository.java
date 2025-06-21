package timetogeter.context.time.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timetogeter.context.time.domain.entity.PromiseTime;
import timetogeter.context.time.domain.repository.custom.PromiseTimeRepositoryCustom;

public interface PromiseTimeRepository extends JpaRepository<PromiseTime, Integer>, PromiseTimeRepositoryCustom {
}
