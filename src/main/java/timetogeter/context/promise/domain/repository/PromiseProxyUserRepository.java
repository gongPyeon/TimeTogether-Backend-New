package timetogeter.context.promise.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.promise.domain.entity.PromiseProxyUser;

@Repository
public interface PromiseProxyUserRepository extends JpaRepository<PromiseProxyUser, String> {
}
