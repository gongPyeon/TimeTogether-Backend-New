package timetogeter.context.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.domain.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public List<String> getUserNamesById(List<String> userIds){
        List<String> names = userRepository.findAllNicknameByUserId(userIds);

        return names;
    }
}
