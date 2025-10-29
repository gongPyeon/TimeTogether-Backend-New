package timetogeter.context.promise.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.context.auth.exception.UserNotFoundException;
import timetogeter.context.group.exception.GroupIdNotFoundException;
import timetogeter.context.group.exception.GroupProxyUserNotFoundException;
import timetogeter.context.group.exception.GroupShareKeyNotFoundException;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise1Request;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise2Request;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise3Request;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise4Request;
import timetogeter.context.promise.application.dto.request.manage.*;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise1Response;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise2Response;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise3Response;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise4Response;
import timetogeter.context.promise.application.dto.response.manage.*;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.entity.PromiseProxyUser;
import timetogeter.context.promise.domain.repository.PromiseProxyUserRepository;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.context.promise.domain.entity.PromiseShareKey;
import timetogeter.context.promise.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.promise.exception.PromiseNotFoundException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PromiseManageInfoService {

    private final GroupProxyUserRepository groupProxyUserRepository;
    private final GroupShareKeyRepository groupShareKeyRepository;
    private final GroupRepository groupRepository;

    private final PromiseRepository promiseRepository;
    private final PromiseProxyUserRepository promiseProxyUserRepository;
    private final PromiseShareKeyRepository promiseShareKeyRepository;

    private final UserRepository userRepository;

    private final StringRedisTemplate redisTemplate;


    //약속 만들기 - 기본 정보 입력 Step1 - 메인 서비스 메소드
    @Transactional
    public CreatePromise1Response createPromise1(String userId, CreatePromise1Request request) {
        String encGroupId = request.encGroupId();
        GroupProxyUser groupProxyUserFound = groupProxyUserRepository.findByUserIdAndEncGroupId(userId,encGroupId)
                .orElseThrow(() -> new GroupProxyUserNotFoundException(BaseErrorCode.GROUP_PROXY_USER_NOT_FOUND, "[ERROR]: 해당 유저의 그룹 프록시 정보가 없습니다."));

        /*
        '개인키로 암호화된 그룹 아이디'-> encGroupId
        '개인키로 암호화한 (그룹키로 암호화한 사용자 고유 아이디)' -> encGroupMemberId
         */
        return new CreatePromise1Response(
                groupProxyUserFound.getEncGroupId(),
                groupProxyUserFound.getEncGroupMemberId()
        );
    }

    //약속 만들기 - 기본 정보 입력 Step2 - 메인 서비스 메소드
    @Transactional
    public CreatePromise2Response createPromise2(String userId, CreatePromise2Request request) {
        String groupId = request.groupId();
        String encGroupMemberId = request.encGroupMemberId();

        String encGroupKey = groupShareKeyRepository.findEncGroupKey(groupId, encGroupMemberId)
                .orElseThrow(() -> new GroupShareKeyNotFoundException(
                        BaseErrorCode.GROUP_SHARE_KEY_NOT_FOUND,
                        "[ERROR]: 그룹 키 조회에 실패했습니다. groupId=" + groupId + ", encGroupMemberId=" + encGroupMemberId
                ));

        return new CreatePromise2Response(encGroupKey);
    }

    //약속 만들기 - 기본 정보 입력 Step3 - 메인 서비스 메소드
    @Transactional
    public CreatePromise3Response createPromise3(CreatePromise3Request request) {


        String groupId = request.groupId();

        // 그룹 정보 조회
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupIdNotFoundException(BaseErrorCode.GROUP_ID_NOTFOUND, "[ERROR]: 존재하지 않는 그룹입니다: " + groupId));

        // 해당 그룹에 속한 사용자 encId 목록 조회
        List<String> encUserIdList = groupShareKeyRepository.findEncUserIdsByGroupId(groupId);

        // 응답 객체 생성
        CreatePromise3Response response = new CreatePromise3Response(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupImg(),
                group.getManagerId(),
                encUserIdList
        );


        return response;
    }

    //약속 만들기 - 기본 정보 입력 Step4 - 메인 서비스 메소드
    @Transactional
    public CreatePromise4Response createPromise4(String userId, CreatePromise4Request request) {

        //Promise에 약속(임의)저장하기
        Promise savedPromise = createPromise(request);
        return new CreatePromise4Response(savedPromise.getPromiseId());
    }

    //약속 만들기 - 기본 정보 입력 - 서브 서비스 메소드(1)
    @Transactional
    public Promise createPromise(CreatePromise4Request request) {
        Promise promise = Promise.of(
                request.groupId(),
                request.title(),
                request.type(),
                request.promiseImg(),
                request.managerId(),
                request.startDate(),
                request.endDate()
        );

        return promiseRepository.save(promise);
    }


    //약속 만들기 - 초대하기 Step1 - 메인 서비스 메소드
    @Transactional
    public InvitePromise1Response invitePromise1(InvitePromise1Request request) {
        List<Map<String, Integer>> whichUserIdIn = request.whichUserIdIn();

        List<String> emailList = new ArrayList<>();

        for (Map<String, Integer> userMap : whichUserIdIn) {
            for (String userId : userMap.keySet()) {
                // userRepository에서 userId로 이메일 조회
                String email = userRepository.findByUserId(userId)
                        .orElseThrow(() -> new UserNotFoundException(BaseErrorCode.INVALID_USER, "[ERROR]: 존재하지 않는 유저입니다."))
                        .getEmail();
                emailList.add(email);
            }
        }

        return new InvitePromise1Response(emailList);
    }

    //약속 만들기 - 참여하기 Step1 - 메인 서비스 메소드
    @Transactional
    public JoinPromise1Response joinPromise1(String userId, JoinPromise1Request request) {
        //약속을 만든 userId 사용자를 PromiseProxyUser 테이블에 저장
        //약속을 만든 userId 사용자를 PromiseShareKey 테이블에 저장
        saveCreatorToPromiseTables(userId, request);

        Promise promise = promiseRepository.findById(request.promiseId())
                .orElseThrow(() -> new PromiseNotFoundException(BaseErrorCode.PROMISE_NOT_FOUND,
                        "promiseId=" + request.promiseId() + " 약속을 찾을 수 없습니다"
                ));

        String promiseName = promise.getTitle(); //약속 제목

        return new JoinPromise1Response(promiseName + " 약속에 참여하였습니다.");
    }

    //약속 만들기 - 참여하기 Step1 - 서브 서비스 메소드(1)
    private void saveCreatorToPromiseTables(String userId, JoinPromise1Request request) {
        // PromiseProxyUser 저장
        promiseProxyUserRepository.save(PromiseProxyUser.of(
                userId,
                request.encPromiseId(),
                LocalDateTime.now(),
                request.encPromiseMemberId()
        ));

        // PromiseShareKey 저장
        promiseShareKeyRepository.save(PromiseShareKey.of(
                request.promiseId(),
                request.encUserId(),
                request.encPromiseKey(),
                UUID.randomUUID().toString()
        ));
    }

}
