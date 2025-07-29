package timetogeter.context.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.context.promise.application.dto.UserInfoDTO;
import timetogeter.context.promise.application.dto.response.UserIdsResDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public List<String> getUserNamesById(List<String> userIds){
        List<String> names = userRepository.findAllNicknameByUserId(userIds);

        return names;
    }

    public List<UserInfoDTO> getUserInfoByDTO(List<String> userIds) {
        return userRepository.findAllNicknameAndImgByUserId(userIds);
    }
}
