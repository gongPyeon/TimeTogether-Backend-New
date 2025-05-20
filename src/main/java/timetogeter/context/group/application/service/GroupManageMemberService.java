package timetogeter.context.group.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.dto.request.JoinGroupRequestDto;
import timetogeter.context.group.application.dto.response.CreateGroupResponseDto;
import timetogeter.context.group.application.dto.response.JoinGroupResponseDto;
import timetogeter.context.group.application.exception.GroupIdNotFoundException;
import timetogeter.context.group.application.exception.GroupShareKeyException;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GroupManageMemberService {
    private final GroupRepository groupRepository;
    private final GroupProxyUserRepository groupProxyUserRepository;
    private final GroupShareKeyRepository groupShareKeyRepository;

    //그룹 초대되기
    @Transactional
    public JoinGroupResponseDto joinGroup(JoinGroupRequestDto request, String invitedId) throws Exception{
        //1.Group 테이블에서 request.groupId()로 찾기
        Group groupFound = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new GroupIdNotFoundException(BaseErrorCode.GROUP_ID_NOTFOUND, "[ERROR] 존재하지 않는 그룹 ID 입니다."));

        //2.GroupProxyUser 테이블에 저장
        createGroupProxyUser(groupFound, invitedId, request.personalMasterKey());
        log.info("[GroupProxyUser]에 저장 완료 - 그룹 초대");

        //3. GroupShareKey 테이블에 저장
        createGroupShareKey(groupFound, request.groupKey(), invitedId, request.personalMasterKey());
        log.info("[GroupShareKey]에 저장 완료 - 그룹 초대");

        //4. groupFound에 있는 그룹원 수 찾기
        Long groupPeopleNum = groupShareKeyRepository.countGroupShareKeyByGroupId(groupFound.getGroupId());

        return new JoinGroupResponseDto(
                groupFound.getGroupId(),
                groupFound.getGroupName(),
                groupFound.getGroupImg(),
                invitedId,
                groupPeopleNum
        );
    }

    //3
    private void createGroupShareKey(Group group, String groupKey, String invitedId, String personalMasterKey) {
        try {

            //1. 그룹키(groupKey)로 암호화한 사용자 고유 아이디
            String encUserId = EncryptUtil.encryptAESGCM(invitedId, groupKey);

            //4. 개인키로 암호화한 그룹키(groupKey)
            String encGroupKey = EncryptUtil.encryptAESGCM(groupKey,personalMasterKey);

            //5. groupShareKey 테이블에 저장
            GroupShareKey groupShareKey = GroupShareKey.of(group.getGroupId(), encUserId, encGroupKey);
            groupShareKeyRepository.save(groupShareKey);

        } catch (Exception e) {
            throw new GroupShareKeyException(BaseErrorCode.GROUP_SHARE_KEY_INTERNAL_ERROR, "[ERROR] GroupShareKey 테이블에 저장되는 로직에서 에러가 발생했습니다.");
        }
    }

    //2
    private void createGroupProxyUser(Group group, String invitedId, String personalMasterKey) throws Exception{
        String encGroupId = encryptWithKey(group.getGroupId(), personalMasterKey);//개인키로 암호화한 그룹 아이디
        String encGroupMemberId = encryptWithKey(group.getManagerId(), personalMasterKey);//개인키로 암호화한 사용자의 아이디
        long timestamp = System.currentTimeMillis();

        /*log.info("[createGroupProxyUser] invitedId: {}", invitedId);
        log.info("[createGroupProxyUser] encGroupId: {}", encGroupId);
        log.info("[createGroupProxyUser] encGroupMemberId: {}", encGroupMemberId);
        log.info("[createGroupProxyUser] timestamp: {}", timestamp);*/

        GroupProxyUser proxyUser = GroupProxyUser.of(
                invitedId, //사용자 고유 아이디
                encGroupId, //개인키로 암호화한 그룹 아이디
                encGroupMemberId, //개인키로 암호화한 사용자 아이디
                timestamp //타임스탬프
        );

        groupProxyUserRepository.save(proxyUser);
        //log.info("[createGroupProxyUser] GroupProxyUser 저장 완료: {}", proxyUser);
    }

    private String encryptWithKey(String data, String key) throws Exception{
        return EncryptUtil.encryptAESGCM(data,key);
    }
}
