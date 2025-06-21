package timetogeter.context.place.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import timetogeter.context.place.domain.entity.PlaceBoard;
import timetogeter.context.place.domain.repository.custom.PlaceBoardRepositoryCustom;

import java.util.List;

public interface PlaceBoardRepository extends JpaRepository<PlaceBoard, Integer>, PlaceBoardRepositoryCustom {
}
