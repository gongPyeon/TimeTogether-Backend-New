package timetogeter.context.place.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.place.application.dto.request.UserBoardReqDTO;
import timetogeter.context.place.application.dto.response.PlaceBoardDTO;
import timetogeter.context.place.domain.entity.UserBoard;
import timetogeter.context.place.domain.repository.UserBoardRepository;

@RequiredArgsConstructor
@Service
public class UserBoardService {

    private final UserBoardRepository userBoardRepository;

    @Transactional
    public void updatePlaceRating(int placeId, UserBoardReqDTO dto) {
        UserBoard userBoard = userBoardRepository.findByPlaceBoardIdAndUserId(placeId, dto.pseudoId())
                .orElseGet(() -> UserBoard.of(placeId, dto.pseudoId(), dto.rating()));

        userBoard.update(placeId, dto.pseudoId(), dto.rating());
        userBoardRepository.save(userBoard);
    }

}
