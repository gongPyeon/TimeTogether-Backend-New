package timetogeter.context.group.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogeter.context.group.domain.entity.GroupProxyUser;

@Repository
public interface GroupProxyUserRepository extends JpaRepository<GroupProxyUser, String> {
}