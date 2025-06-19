package timetogeter.context.schedule.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.schedule.domain.entity.PromiseShareKey;

import java.util.List;

@Repository
public interface PromiseShareKeyRepository extends JpaRepository<PromiseShareKey, String> {

    List<PromiseShareKey> findByPromiseId(String promiseId);
}
