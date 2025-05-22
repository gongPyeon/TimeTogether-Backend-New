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
import timetogeter.context.group.application.exception.GroupIdNotFoundException;
import timetogeter.context.group.application.exception.GroupInviteCodeExpired;
import timetogeter.context.group.application.exception.GroupProxyUserNotFoundException;
import timetogeter.context.group.application.exception.GroupShareKeyNotFoundException;
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
    public InviteGroup1Response inviteGroup1(InviteGroup1Request request) {
        String groupId = request.groupId();
        String encGroupId = request.encGroupId();

        GroupProxyUser groupProxyUser = groupProxyUserRepository.findByGroupIdAndEncGroupId(groupId, encGroupId)
                .orElseThrow(() -> new GroupProxyUserNotFoundException(BaseErrorCode.GROUP_PROXY_USER_NOT_FOUND, "[ERROR]: 해당 그룹 프록시 정보가 없습니다."));
        String encEncGroupMemberId = groupProxyUser.getEncGroupMemberId();

        return new InviteGroup1Response(encEncGroupMemberId);
    }

    //그룹 상세 - 그룹 초대하기 - step2 - 메인 서비스 메소드
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
    public String inviteGroup3(InviteGroup3Request request) {
        String validInviteCodeCheck = request.randomKeyForRedis();

        redisTemplate.opsForValue().set("INVITE_KEY:" + validInviteCodeCheck, validInviteCodeCheck, Duration.ofMinutes(60)); // 60분 유효

        return "발급하신 초대코드의 유효기한 : 60분";
    }

//======================
// 그룹 관리 - 그룹 초대받기 (Step1)
//======================

    //그룹 관리 - 그룹 초대받기 - step1 - 메인 서비스 메소드
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

    // TODO: 프론트 코드 생성시 참고할 예정 (잠시 주석처리)
/*
    //그룹 초대되기 - redis
    public JoinGroupInnerRequestDto getRequestDto(JoinGroupRequestDto request) throws Exception {
        String token = request.token();

        // 1. Redis에서 UUID 가져오기 (key = "INVITE_KEY:" + 앞6 + 뒤6)
        String keyFragment = token.substring(0, 6) + token.substring(token.length() - 6);
        String uuid = redisTemplate.opsForValue().get("INVITE_KEY:" + keyFragment);

        if (uuid == null) {
            throw new GroupInviteCodeExpired(BaseErrorCode.GROUP_INVITECODE_EXPIRED, "[ERROR] 초대코드가 유효하지 않거나 만료되었습니다.");
        }

        // 2. UUID를 키로 token 복호화
        String decrypted = EncryptUtil.decryptAESGCM(token, uuid); // 예: groupId=xxx&groupKey=yyy

        // 3. groupId, groupKey 추출
        Map<String, String> paramMap = Arrays.stream(decrypted.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));

        String groupId = paramMap.get("groupId");
        String groupKey = paramMap.get("groupKey");

        // 4. JoinGroupInnerRequestDto 생성 및 반환
        return new JoinGroupInnerRequestDto(groupId, groupKey,request.personalMasterKey());
    }

    //그룹 초대되기
    @Transactional
    public JoinGroupResponseDto joinGroup(JoinGroupInnerRequestDto request, String invitedId) throws Exception{
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
            throw new GroupShareKeyNotFoundException(BaseErrorCode.GROUP_SHARE_KEY_INTERNAL_ERROR, "[ERROR] GroupShareKey 테이블에 저장되는 로직에서 에러가 발생했습니다.");
        }
    }

    //2
    private void createGroupProxyUser(Group group, String invitedId, String personalMasterKey) throws Exception{
        String encGroupId = encryptWithKey(group.getGroupId(), personalMasterKey);//개인키로 암호화한 그룹 아이디
        String encGroupMemberId = encryptWithKey(group.getManagerId(), personalMasterKey);//개인키로 암호화한 사용자의 아이디
        long timestamp = System.currentTimeMillis();

        */
/*log.info("[createGroupProxyUser] invitedId: {}", invitedId);
        log.info("[createGroupProxyUser] encGroupId: {}", encGroupId);
        log.info("[createGroupProxyUser] encGroupMemberId: {}", encGroupMemberId);
        log.info("[createGroupProxyUser] timestamp: {}", timestamp);*//*


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

    //=======================================================================

    //그룹 초대코드 만들기
    @Transactional
    public InviteGroupInfoResponseDto inviteGroup(InviteGroupInfoRequestDto request, String userId) throws Exception {

        //0. 그룹 아이디(reqeust.groupId()) , 사용자의 마스터키(request.personalMasterKey()
        String groupId = request.groupId();
        String masterKey = request.personalMasterKey();

        //1. 해당 사용자가 그룹 내 구성원인지 확인
        boolean isGroupMember = isGroupMember(groupId, userId, masterKey);
        if (!isGroupMember) {
            throw new GroupManagerMissException(BaseErrorCode.NOT_GROUO_MEMBER_ERROR, "[ERROR]: 그룹 멤버가 아니므로 그룹 초대코드를 생성할 수 없습니다.");
        }

        */
/**
         * 초대링크 : "생 그룹키", "생 그룹 아이디" 가 재료
         *//*


        //2. 복호화한 그룹키 반환
        String groupKey = groupManageDisplayService.getGroupKeyList(List.of(groupId),masterKey).get(0);

        //3. 초대링크 생성
        String inviteUrl = createInviteUrl(groupId, groupKey);

        return new InviteGroupInfoResponseDto(
                inviteUrl
        );
    }

    //3
    public String createInviteUrl(String groupId, String groupKey) throws Exception {
        //3-1. 초대에 사용할 랜덤 UUID 생성
        String aesKey = EncryptUtil.generateRandomBase64AESKey();

        //3-2. inviteData 구성
        String plainData = "groupId=" + groupId + "&groupKey=" + groupKey;

        //3-3. UUID를 키로 AES-GCM 암호화
        String encrypted = EncryptUtil.encryptAESGCM(plainData, aesKey);

        //3-4. Redis에 uuid 저장 (사용자 토큰 복호화용, 시간 제한도 가능)
        String keyFragment = encrypted.substring(0, 6) + encrypted.substring(encrypted.length() - 6);
        redisTemplate.opsForValue().set("INVITE_KEY:" + keyFragment, aesKey, Duration.ofMinutes(60)); // 60분 유효

        //3-5. 최종 URL 반환
        return "http://localhost:8080/group/join?token=" + encrypted;
    }


    //1
    private boolean isGroupMember(String groupId, String userId, String masterKey) throws Exception {
        // 1-1. groupProxyUserRepository에서 groupId에 해당하는 레코드 리스트 반환
        List<GroupShareKey> shareKeys = groupShareKeyRepository.findAllByGroupId(groupId);

        for (GroupShareKey shareKey : shareKeys) {
            try {
                // 1-2-1. encGroupKey를 masterKey로 복호화 → 복호화 실패 시 catch
                String groupKey = EncryptUtil.decryptAESGCM(shareKey.getEncGroupKey(), masterKey);

                // 1-2-2. encUserId를 groupKey로 복호화
                String decryptedUserId = EncryptUtil.decryptAESGCM(shareKey.getEncUserId(), groupKey);

                // 1-2-3. 복호화한 userId가 현재 사용자와 일치하면 true 반환
                if (decryptedUserId.equals(userId)) {
                    return true;
                }

            } catch (Exception e) {
                // 복호화 실패 시 무시하고 다음 레코드 검사
                continue;
            }
        }

        // 모두 검사했지만 해당 userId가 없으면 false
        return false;
    }
*/


}
