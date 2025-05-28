package timetogeter.context.promise.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.dto.request.ViewGroup3Request;
import timetogeter.context.group.application.dto.response.ViewGroup1Response;
import timetogeter.context.group.application.dto.response.ViewGroup3Response;
import timetogeter.context.group.application.exception.GroupIdNotFoundException;
import timetogeter.context.group.application.exception.GroupProxyUserNotFoundException;
import timetogeter.context.group.application.exception.GroupShareKeyNotFoundException;
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;
import timetogeter.context.promise.application.dto.request.CreatePromiseAlimRequest1;
import timetogeter.context.promise.application.dto.request.CreatePromiseViewRequest1;
import timetogeter.context.promise.application.dto.request.CreatePromiseViewRequest2;
import timetogeter.context.promise.application.dto.request.CreatePromiseViewRequest3;
import timetogeter.context.promise.application.dto.response.CreatePromiseAlimResponse1;
import timetogeter.context.promise.application.dto.response.CreatePromiseViewResponse1;
import timetogeter.context.promise.application.dto.response.CreatePromiseViewResponse2;
import timetogeter.context.promise.application.dto.response.CreatePromiseViewResponse3;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.context.promise.domain.repository.PromiseRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PromiseManageInfoService {

    private GroupProxyUserRepository groupProxyUserRepository;
    private GroupShareKeyRepository groupShareKeyRepository;
    private GroupRepository groupRepository;

    private PromiseRepository promiseRepository;

    //약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step1 - 메인 서비스 메소드
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

    //약속 만들기 - 기본 정보 입력 및 수락 알림 보내기 Step1 - 메인 서비스 메소드
    public CreatePromiseAlimResponse1 createPromise1(String userId, CreatePromiseAlimRequest1 request) {
        //1. Promise에 약속(임의)저장하기
        createPromise(request);

        //2. 각 예비 약속원에게 수락알림 보내기
        sendAlim();

        return new CreatePromiseAlimResponse1("약속원에게 수락알림이 전송되었습니다.");
    }

    //약속 만들기 - 기본 정보 입력 및 수락 알림 보내기 Step1 - 서브 서비스 메소드(2)
    private void sendAlim() {
        //구현예정
    }

    //약속 만들기 - 기본 정보 입력 및 수락 알림 보내기 Step1 - 서브 서비스 메소드(1)
    private void createPromise(CreatePromiseAlimRequest1 request) {
        Promise promise = Promise.of(
                request.groupId(),
                request.title(),
                request.type(),
                request.promiseImg(),
                request.encManagerId(),
                request.startDate(),
                request.endDate()

        );

        promiseRepository.save(promise);
    }
}
