package timetogeter.context.place.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import timetogeter.context.place.domain.entity.Place;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, String> {
    Optional<Place> findByPlaceId(int placeId);

    Page<Place> findByPromiseId(int promiseId, PageRequest pageRequest);


    @Query("SELECT p FROM Place p WHERE p.promiseId = :promiseId AND p.isConfirmed = true")
    Optional<Place> findConfirmPlaceById(String promiseId);
}
