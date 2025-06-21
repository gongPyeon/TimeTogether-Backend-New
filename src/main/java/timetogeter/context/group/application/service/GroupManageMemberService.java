package timetogeter.context.group.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.dto.request.InviteGroup1Request;
import timetogeter.context.group.application.dto.request.InviteGroup2Request;
import timetogeter.context.group.application.dto.request.InviteGroup3Request;
import timetogeter.context.group.application.dto.request.JoinGroup1Request;
import timetogeter.context.group.application.dto.response.InviteGroup1Response;
import timetogeter.context.group.application.dto.response.InviteGroup2Response;
import timetogeter.context.group.application.dto.response.JoinGroup1Response;
import timetogeter.context.group.exception.GroupIdNotFoundException;
import timetogeter.context.group.exception.GroupInviteCodeExpired;
import timetogeter.context.group.exception.GroupProxyUserNotFoundException;
import timetogeter.context.group.exception.GroupShareKeyNotFoundException;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GroupManageMemberService {
    private final GroupRepository groupRepository;
    private final GroupProxyUserRepository groupProxyUserRepository;
    private final GroupShareKeyRepository groupShareKeyRepository;

    private final GroupManageDisplayService groupManageDisplayService;

    private final StringRedisTemplate redisTemplate;

//======================
// 그룹 상세 - 그룹 초대하기 (Step1,2,3)
//======================

    //그룹 상세 - 그룹 초대하기 - step1 - 메인 서비스 메소드
    @Transactional
    public InviteGroup1Response inviteGroup1(InviteGroup1Request request,String userId) {
        String groupId = request.groupId();
        String encGroupId = request.encGroupId();

        GroupProxyUser groupProxyUser = groupProxyUserRepository.findByUserIdAndEncGroupId(userId, encGroupId)
                .orElseThrow(() -> new GroupProxyUserNotFoundException(BaseErrorCode.GROUP_PROXY_USER_NOT_FOUND, "[ERROR]: 해당 그룹 프록시 정보가 없습니다."));
        String encEncGroupMemberId = groupProxyUser.getEncGroupMemberId();

        return new InviteGroup1Response(encEncGroupMemberId);
    }

    //그룹 상세 - 그룹 초대하기 - step2 - 메인 서비스 메소드
    @Transactional
    public InviteGroup2Response inviteGroup2(InviteGroup2Request request) {
        //encUserId, groupId 로 GroupShareKey 요청
        String encUserId = request.encUserId();
        String groupId = request.groupId();

        String encGroupKey = groupShareKeyRepository.findEncGroupKey(groupId, encUserId)
                .orElseThrow(() -> new GroupShareKeyNotFoundException(
                        BaseErrorCode.GROUP_SHARE_KEY_NOT_FOUND,
                        "[ERROR]: 그룹 키 조회에 실패했습니다. groupId=" + groupId + ", encGroupMemberId=" + encUserId
                ));

        return new InviteGroup2Response(encGroupKey);
    }

    //그룹 상세 - 그룹 초대하기 - step3 - 메인 서비스 메소드
    @Transactional
    public String inviteGroup3(InviteGroup3Request request) {
        String validInviteCodeCheck = request.randomKeyForRedis();

        redisTemplate.opsForValue().set("INVITE_KEY:" + validInviteCodeCheck, validInviteCodeCheck, Duration.ofMinutes(60)); // 60분 유효

        return "발급하신 초대코드의 유효기한 : 60분";
    }

//======================
// 그룹 관리 - 그룹 초대받기 (Step1)
//======================

    //그룹 관리 - 그룹 초대받기 - step1 - 메인 서비스 메소드
    @Transactional
    public JoinGroup1Response joinGroup1(JoinGroup1Request request,String userId) {
        String randomUUID = request.randomUUID();
        String storedUUID = redisTemplate.opsForValue().get("INVITE_KEY:" + randomUUID);
        if (storedUUID == null){
            throw new GroupInviteCodeExpired(BaseErrorCode.GROUP_INVITECODE_EXPIRED, "[ERROR]: 초대코드가 만료되었습니다.");
        }
        String groupId = request.groupId();
        String encGroupKey = request.encGroupKey();
        String encUserId = request.encUserId();
        String encGroupId = request.encGroupId();
        String encencGroupMemberId = request.encencGroupMemberId();

        //GroupProxyUser에 저장
        groupProxyUserRepository.save(GroupProxyUser.of(userId,encGroupId,encencGroupMemberId,System.currentTimeMillis()));

        //GroupShareKey에 저장
        groupShareKeyRepository.save(GroupShareKey.of(groupId, encUserId, encGroupKey));

        //그룹 이름
        Group joinedGroupName = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupIdNotFoundException(BaseErrorCode.GROUP_ID_NOTFOUND, "[ERROR]: 존재하지 않는 그룹입니다: " + groupId));
        return new JoinGroup1Response(joinedGroupName.getGroupName() + "에 참여했어요.");
    }


}
