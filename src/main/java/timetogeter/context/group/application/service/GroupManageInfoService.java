package timetogeter.context.group.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.dto.request.CreateGroupRequestDto;
import timetogeter.context.group.application.dto.request.EditGroupInfoRequestDto;
import timetogeter.context.group.application.dto.response.CreateGroupResponseDto;
import timetogeter.context.group.application.dto.response.EditGroupInfoResponseDto;
import timetogeter.context.group.application.dto.response.ViewGroupInfoDto;
import timetogeter.context.group.application.exception.GroupIdNotFoundException;
import timetogeter.context.group.application.exception.GroupManagerMissException;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GroupManageInfoService {

    private final GroupManageDisplayService groupManageDisplayService;

    private final GroupRepository groupRepository;
    private final GroupProxyUserRepository groupProxyUserRepository;
    private final GroupShareKeyRepository groupShareKeyRepository;

    //그룹 만들기
    @Transactional
    public CreateGroupResponseDto createGroup(CreateGroupRequestDto request, String managerId) throws Exception{
        //1.Group 테이블에 저장
        Group group = Group.of(request.groupName(), request.groupImg(), managerId);
        groupRepository.save(group);

        //2.GroupProxyUser 테이블에 저장
        createGroupProxyUser(group, managerId, request.personalMasterKey());

        //3. GroupShareKey 테이블에 저장
        createGroupShareKey(group, request.personalMasterKey());

        return new CreateGroupResponseDto(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupImg(),
                group.getManagerId()
        );
    }

    //3
    private void createGroupShareKey(Group group, String personalMasterKey) {
        try {
            //1. 그룹키(groupKey) 생성 (Base64)
            String groupKey = EncryptUtil.generateRandomBase64AESKey();

            log.info("그룹키 : " + groupKey);

            //2. 사용자 고유 아이디
            String userId = group.getManagerId();  //여기서는 managerId

            //3. 그룹키(groupKey)로 암호화한 사용자 고유 아이디
            String encUserId = EncryptUtil.encryptAESGCM(userId, groupKey);

            //4. 개인키로 암호화한 그룹키(groupKey)
            String encGroupKey = EncryptUtil.encryptAESGCM(groupKey,personalMasterKey);

            //5. groupShareKey 테이블에 저장
            GroupShareKey groupShareKey = GroupShareKey.of(group.getGroupId(), encUserId, encGroupKey);
            groupShareKeyRepository.save(groupShareKey);

        } catch (Exception e) {
            throw new RuntimeException("그룹 공유 키 생성 중 오류", e);
        }
    }

    //2
    private void createGroupProxyUser(Group group, String managerId, String personalMasterKey) throws Exception{
        String encGroupId = encryptWithKey(group.getGroupId(), personalMasterKey);//개인키로 암호화한 그룹 아이디
        String encGroupMemberId = encryptWithKey(managerId, personalMasterKey);//개인키로 암호화한 사용자의 아이디
        long timestamp = System.currentTimeMillis();

        GroupProxyUser proxyUser = GroupProxyUser.of(
                managerId, //사용자 고유 아이디
                encGroupId, //개인키로 암호화한 그룹 아이디
                encGroupMemberId, //개인키로 암호화한 사용자 아이디
                timestamp //타임스탬프
        );

        groupProxyUserRepository.save(proxyUser);
    }

    private String encryptWithKey(String data, String key) throws Exception{
        return EncryptUtil.encryptAESGCM(data,key);
    }


    //=======================================================================


    //그룹 정보 수정하기
    @Transactional
    public EditGroupInfoResponseDto editGroup(EditGroupInfoRequestDto request, String managerId) {
        //0. 그룹 아이디(reqeust.groupId()) , 사용자의 마스터키(request.personalMasterKey()
        String groupId = request.groupId();
        String masterKey = request.personalMasterKey();

        //1. 해당 사용자가 관리자 권한이 있는 방장인지 확인
        boolean isGroupManager = groupRepository.existsByGroupIdAndManagerId(groupId, managerId);
        if (!isGroupManager) {
            throw new GroupManagerMissException(BaseErrorCode.NOT_GROUO_MANAGER_ERROR, "[ERROR]: 그룹 방장이 아니므로 그룹 정보를 수정할 수 없습니다.");
        }

        //2. 그룹 정보 가져오기
        Group groupFound = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupIdNotFoundException(BaseErrorCode.GROUP_ID_NOTFOUND,"존재하는 그룹 ID가 아닙니다."));

        //3. 그룹 정보 수정 (이름, 이미지)
        updateGroupInfo(groupFound, request);

        //4. 그룹 멤버 리스트로 반환
        List<String> groupMembers = getGroupMembers(groupId, masterKey);

        //5. 응답 반환
        return new EditGroupInfoResponseDto(
                groupFound.getGroupId(),
                groupFound.getGroupName(),
                groupFound.getGroupImg(),
                groupFound.getManagerId(),
                groupMembers
        );
    }

    //4
    private List<String> getGroupMembers(String groupId, String masterKey) {

        //4-1. 개인키로 '개인키로 암호화한 그룹키(encGroupKey)' 복호화 하기
        List<String> groupKeyList = groupManageDisplayService.getGroupKeyList(List.of(groupId), masterKey);

        //4-2. 그룹 아이디로 '그룹키로 암호화한 사용자 고유 아이디'(encUserId) 리스트 찾기
        List<String> encUserIdListPerGroup = groupManageDisplayService.getEncUserIdListPerGroup(List.of(groupId)).get(0);

        //4-3. 그룹키로 '사용자 고유 아이디' 복호화 하여 리스트 찾기
        List<String> decryptUserIdList = groupManageDisplayService.getDecryptUserIdListPerGroup(List.of(encUserIdListPerGroup), groupKeyList).get(0);

        return decryptUserIdList;
    }

    //3
    private void updateGroupInfo(Group groupFound, EditGroupInfoRequestDto request) {
        groupFound.updateName(request.groupName());
        groupFound.updateImg(request.groupImg());

        groupRepository.save(groupFound);
    }


}
