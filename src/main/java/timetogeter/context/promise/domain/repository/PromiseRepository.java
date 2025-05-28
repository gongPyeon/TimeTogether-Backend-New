package timetogeter.context.promise.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.entity.Promise;

@Repository
public interface PromiseRepository extends JpaRepository<Promise, String> {
}
