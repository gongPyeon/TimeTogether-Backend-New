package timetogeter.context.promise.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.service.UserQueryService;
import timetogeter.context.promise.application.dto.UserInfoDTO;
import timetogeter.context.promise.application.dto.request.ExitPromiseReqDTO;
import timetogeter.context.promise.application.dto.response.UserInfoResDTO;
import timetogeter.context.promise.domain.repository.PromiseProxyUserRepository;
import timetogeter.context.promise.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.promise.application.dto.response.UserIdsResDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromiseSecurityService {
    private final PromiseShareKeyRepository promiseShareKeyRepository;
    private final PromiseProxyUserRepository promiseProxyUserRepository;
    private final PromiseQueryService promiseQueryService;
    private final UserQueryService userQueryService;

    public UserIdsResDTO getUsersByPromiseTime(String promiseId) {
        List<String> userIds = promiseShareKeyRepository.findUserIdsByPromiseId(promiseId);

        return new UserIdsResDTO(userIds);
    }

    public UserInfoResDTO getUserInfoByDTO(String promiseId, UserIdsResDTO reqDTO) {
        String promiseManager = promiseQueryService.getPromiseManager(promiseId);
        List<UserInfoDTO> users = userQueryService.getUserInfoByDTO(reqDTO.userIds());

        return new UserInfoResDTO(promiseManager, users);
    }

    @Transactional
    public void exitPromise(ExitPromiseReqDTO reqDTO) {
        promiseProxyUserRepository.findByEncPromiseId(reqDTO.encPromiseId()).ifPresent(promiseProxyUserRepository::delete);
        promiseShareKeyRepository.findByEncPromiseKey(reqDTO.encPromiseKey()).ifPresent(promiseShareKeyRepository::delete);
    }
}
