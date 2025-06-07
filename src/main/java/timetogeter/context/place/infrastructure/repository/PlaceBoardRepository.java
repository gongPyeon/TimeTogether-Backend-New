package timetogeter.context.place.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import timetogeter.context.place.domain.entity.PlaceBoard;

import java.util.List;

public interface PlaceBoardRepository extends JpaRepository<PlaceBoard, Integer> {

    List<PlaceBoard> findByUserId(String userId);
}
