package timetogeter.context.group.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.dto.request.CreateGroupRequestDto;
import timetogeter.context.group.application.dto.response.CreateGroupResponseDto;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.entity.GroupShareKey;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GroupManageInfoService {
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


}
