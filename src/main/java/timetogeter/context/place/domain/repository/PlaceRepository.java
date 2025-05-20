package timetogeter.context.place.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import timetogeter.context.place.domain.entity.Place;
import timetogeter.context.place.domain.repository.custom.PlaceRepositoryCustom;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, String>, PlaceRepositoryCustom {
    Optional<Place> findByPlaceId(int placeId);

    Page<Place> findByPromiseId(int promiseId, PageRequest pageRequest);
}
