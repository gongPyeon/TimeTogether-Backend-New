package timetogeter.context.promise.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.exception.GroupIdNotFoundException;
import timetogeter.context.group.application.exception.GroupProxyUserNotFoundException;
import timetogeter.context.group.application.exception.GroupShareKeyNotFoundException;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;
import timetogeter.context.promise.application.dto.request.*;
import timetogeter.context.promise.application.dto.response.*;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.entity.PromiseProxyUser;
import timetogeter.context.promise.domain.repository.PromiseProxyUserRepository;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.context.schedule.domain.entity.PromiseShareKey;
import timetogeter.context.schedule.domain.repository.PromiseShareKeyRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final StringRedisTemplate redisTemplate;


    //약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step1 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseViewResponse1 createPromiseView1(String userId, CreatePromiseViewRequest1 request) {
        String encGroupId = request.encGroupId();
        GroupProxyUser groupProxyUserFound = groupProxyUserRepository.findByUserIdAndEncGroupId(userId,encGroupId)
                .orElseThrow(() -> new GroupProxyUserNotFoundException(BaseErrorCode.GROUP_PROXY_USER_NOT_FOUND, "[ERROR]: 해당 유저의 그룹 프록시 정보가 없습니다."));

        /*
        '개인키로 암호화된 그룹 아이디'-> encGroupId
        '개인키로 암호화한 (그룹키로 암호화한 사용자 고유 아이디)' -> encGroupMemberId
         */
        return new CreatePromiseViewResponse1(
                groupProxyUserFound.getEncGroupId(),
                groupProxyUserFound.getEncGroupMemberId()
        );
    }

    //약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step2 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseViewResponse2 createPromiseView2(String userId, CreatePromiseViewRequest2 request) {
        String groupId = request.groupId();
        String encGroupMemberId = request.encGroupMemberId();

        String encGroupKey = groupShareKeyRepository.findEncGroupKey(groupId, encGroupMemberId)
                .orElseThrow(() -> new GroupShareKeyNotFoundException(
                        BaseErrorCode.GROUP_SHARE_KEY_NOT_FOUND,
                        "[ERROR]: 그룹 키 조회에 실패했습니다. groupId=" + groupId + ", encGroupMemberId=" + encGroupMemberId
                ));

        return new CreatePromiseViewResponse2(encGroupKey);
    }

    //약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step3 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseViewResponse3 createPromiseView3(String userId, CreatePromiseViewRequest3 request) {


        String groupId = request.groupId();

        // 그룹 정보 조회
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupIdNotFoundException(BaseErrorCode.GROUP_ID_NOTFOUND, "[ERROR]: 존재하지 않는 그룹입니다: " + groupId));

        // 해당 그룹에 속한 사용자 encId 목록 조회
        List<String> encUserIdList = groupShareKeyRepository.findEncUserIdsByGroupId(groupId);

        // 응답 객체 생성
        CreatePromiseViewResponse3 response = new CreatePromiseViewResponse3(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupImg(),
                group.getManagerId(),
                encUserIdList
        );


        return response;
    }

    //약속 만들기 - 약속 만들고 알림 보내기 Step1 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseAlimResponse1 createPromise1(String userId, CreatePromiseAlimRequest1 request) {
        String encGroupId = request.encGroupId();

        GroupProxyUser groupProxyUser = groupProxyUserRepository.findByUserIdAndEncGroupId(userId, encGroupId)
                .orElseThrow(() -> new GroupProxyUserNotFoundException(BaseErrorCode.GROUP_PROXY_USER_NOT_FOUND, "[ERROR]: 해당 그룹 프록시 정보가 없습니다."));
        String encEncGroupMemberId = groupProxyUser.getEncGroupMemberId();

        return new CreatePromiseAlimResponse1(encEncGroupMemberId);
    }

    //약속 만들기 - 약속 만들고 알림 보내기 Step2 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseAlimResponse2 createPromise2(CreatePromiseAlimRequest2 request) {
        String groupId = request.groupId();
        String encGroupMemberId = request.encUserId();

        String encGroupKey = groupShareKeyRepository.findEncGroupKey(groupId, encGroupMemberId)
                .orElseThrow(() -> new GroupShareKeyNotFoundException(
                        BaseErrorCode.GROUP_SHARE_KEY_NOT_FOUND,
                        "[ERROR]: 그룹 키 조회에 실패했습니다. groupId=" + groupId + ", encGroupMemberId=" + encGroupMemberId
                ));

        return new CreatePromiseAlimResponse2(encGroupKey);

    }

    //약속 만들기 - 약속 만들고 알림 보내기 Step3 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseAlimResponse3 createPromise3(CreatePromiseAlimRequest3 request) {
        //1. Promise에 약속(임의)저장하기
        Promise savedPromise = createPromise(request);
        return new CreatePromiseAlimResponse3(savedPromise.getPromiseId());
    }


    //약속 만들기 - 약속 만들고 알림 보내기 Step3 - 서브 서비스 메소드(1)
    @Transactional
    public Promise createPromise(CreatePromiseAlimRequest3 request) {
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

    //약속 만들기 - 약속 만들고 알림 보내기 Step4 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseAlimResponse4 createPromise4(CreatePromiseAlimRequest4 request) {

        String groupId = request.groupId();
        List<String> encUserIdList = request.encUserIdList();

        List<String> encGroupKeyList = new ArrayList<>();

        for (String encUserId : encUserIdList) {
            // GroupShareKey에서 encGroupId와 encUserId에 해당하는 encGroupKey를 조회
            String encGroupKey = groupShareKeyRepository.findByEncGroupIdAndEncUserId(groupId, encUserId)
                    .orElseThrow(() -> new GroupShareKeyNotFoundException(BaseErrorCode.GROUP_SHARE_KEY_NOT_FOUND,
                            String.format("encUserId=%s, groupId=%s 에 해당하는 GroupShareKey가 없습니다", encUserId, groupId)));

            encGroupKeyList.add(encGroupKey);
        }

        return new CreatePromiseAlimResponse4(encGroupKeyList);
    }


    //약속 만들기 - 약속 만들고 알림 보내기 Step5 - 메인 서비스 메소드
    @Transactional
    public CreatePromiseAlimResponse5 createPromise5(String userId, CreatePromiseAlimRequest5 request) {
        //1-1. 약속을 만든 userId 사용자를 PromiseProxyUser 테이블에 저장
        //1-2. 약속을 만든 userId 사용자를 PromiseShareKey 테이블에 저장
        saveCreatorToPromiseTables(userId, request);

        //2. 각 예비 약속원이 접속했을때 알림에 뜨도록
        prepareCandidatePromiseMembers(request);

        return new CreatePromiseAlimResponse5("약속 생성 및 알림 전송 완료");
    }

    //약속 만들기 - 약속 만들고 알림 보내기 Step5 - 서브 서비스 메소드(2)
    private void prepareCandidatePromiseMembers(CreatePromiseAlimRequest5 request) {
        List<String> encEncGroupKeyList = request.encEncGroupKeyList();
        List<HashMap<String, Integer>> userMaps = request.whichEncUserIdsIn();

        for (int i = 0; i < userMaps.size(); i++) {
            HashMap<String, Integer> userMap = userMaps.get(i);
            String encEncGroupKey = encEncGroupKeyList.get(i); // userMap 인덱스에 대응되는 groupKey

            for (Map.Entry<String, Integer> entry : userMap.entrySet()) {
                String encUserId = entry.getKey();
                Integer flag = entry.getValue();

                if (flag == 1) { // 참여하는 예비 약속원에 대해서만
                    // Redis 저장 (encEncGroupKey 함께 저장)
                    String redisKey = String.format("promise:notify:%s:%s", encUserId, "PENDING");
                    redisTemplate.opsForValue().set(
                            redisKey,
                            encEncGroupKey+"::"+request.promiseId(),
                            Duration.ofHours(24) // TTL 24시간
                    );

                }
            }
        }
    }


    //약속 만들기 - 약속 만들고 알림 보내기 Step5 - 서브 서비스 메소드(1)
    private void saveCreatorToPromiseTables(String userId, CreatePromiseAlimRequest5 request) {
        // PromiseProxyUser 저장
        promiseProxyUserRepository.save(PromiseProxyUser.of(
                userId,
                request.encPromiseId(),
                LocalDateTime.now(),
                request.encPromiseMemberId()
        ));

        // PromiseShareKey 저장
        promiseShareKeyRepository.save(PromiseShareKey.of(
                userId,
                request.encPromiseId(),
                request.encPromiseKey(),
                null
        ));
    }


    //약속 만들기 - 약속 만들기 알림 수락 Step1 - 메인 서비스 메소드
    @Transactional
    public CreateJoinPromiseResponse1 createJoinPromise1(String userId, CreateJoinPromiseRequest1 request) {
        String encGroupId = request.encGroupId();

        GroupProxyUser groupProxyUser = groupProxyUserRepository.findByUserIdAndEncGroupId(userId, encGroupId)
                .orElseThrow(() -> new GroupProxyUserNotFoundException(BaseErrorCode.GROUP_PROXY_USER_NOT_FOUND, "[ERROR]: 해당 그룹 프록시 정보가 없습니다."));
        String encEncGroupMemberId = groupProxyUser.getEncGroupMemberId();

        return new CreateJoinPromiseResponse1(encEncGroupMemberId);
    }

    //약속 만들기 - 약속 만들기 알림 수락 Step2 - 메인 서비스 메소드
    @Transactional
    public CreateJoinPromiseResponse2 createJoinPromise2(CreateJoinPromiseRequest2 request) {
        String groupId = request.groupId();
        String encGroupMemberId = request.encUserId();

        String encGroupKey = groupShareKeyRepository.findEncGroupKey(groupId, encGroupMemberId)
                .orElseThrow(() -> new GroupShareKeyNotFoundException(
                        BaseErrorCode.GROUP_SHARE_KEY_NOT_FOUND,
                        "[ERROR]: 그룹 키 조회에 실패했습니다. groupId=" + groupId + ", encGroupMemberId=" + encGroupMemberId
                ));

        return new CreateJoinPromiseResponse2(encGroupKey);
    }

    //약속 만들기 - 약속 만들기 알림 수락 Step3 - 메인 서비스 메소드
    @Transactional
    public CreateJoinPromiseResponse3 createJoinPromise3(CreateJoinPromiseRequest3 request) {
        String encUserId = request.encUserId();

        String redisKey = String.format("promise:notify:%s:%s", encUserId, "PENDING");

        // Redis에서 값 조회
        String encEncGroupKey = redisTemplate.opsForValue().get(redisKey);

        if (encEncGroupKey == null) {
            // Redis에 값이 없는 경우 (예: TTL 만료 or 잘못된 요청)
            throw new IllegalStateException("약속 참여 알림 정보가 없습니다.");//TODO: 후에 exception 리팩토링 예정
        }

        //사용 후 Redis에서 삭제
        redisTemplate.delete(redisKey);
        return new CreateJoinPromiseResponse3(encEncGroupKey);
    }

    //약속 만들기 - 약속 만들기 알림 수락 Step4 - 메인 서비스 메소드
    @Transactional
    public CreateJoinPromiseResponse4 createJoinPromise4(String userId, CreateJoinPromiseRequest4 request) {
        //1-1. userId 사용자를 PromiseProxyUser 테이블에 저장
        //1-2. userId 사용자를 PromiseShareKey 테이블에 저장
        saveCreatorToPromiseTables2(userId, request);
        return new CreateJoinPromiseResponse4("약속 참여를 수락하셨습니다.");
    }

    //약속 만들기 - 약속 만들기 알림 수락 Step4 - 서브 서비스 메소드(1)
    private void saveCreatorToPromiseTables2(String userId, CreateJoinPromiseRequest4 request) {
        // PromiseProxyUser 저장
        promiseProxyUserRepository.save(PromiseProxyUser.of(
                userId,
                request.encPromiseId(),
                LocalDateTime.now(),
                request.encPromiseMemberId()
        ));

        // PromiseShareKey 저장
        promiseShareKeyRepository.save(PromiseShareKey.of(
                userId,
                request.encPromiseId(),
                request.encPromiseKey(),
                null
        ));
    }

}
