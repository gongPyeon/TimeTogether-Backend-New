package timetogeter.context.time.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.time.domain.entity.PromiseCheck;
import timetogeter.context.time.infrastructure.repository.PromiseCheckRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeConfirmService {

    private final PromiseCheckRepository promiseCheckRepository;

    public Optional<PromiseCheck> isConfirmedTime(String promiseId){
        return promiseCheckRepository.isConfirmedById(promiseId);
    }

}
