package timetogeter.context.group.application.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import timetogeter.context.group.application.dto.request.*;
import timetogeter.context.group.application.dto.response.*;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GroupManageMemberService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.key}")
    private String s3Key;

    //@Value("${lambda.verify.url}")
    //private String lambdaVerifyUrl;

    private final GroupRepository groupRepository;
    private final GroupProxyUserRepository groupProxyUserRepository;
    private final GroupShareKeyRepository groupShareKeyRepository;

    private final GroupManageDisplayService groupManageDisplayService;

    private final StringRedisTemplate redisTemplate;
    private final RestTemplate restTemplate;

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
    public InviteGroup3Response inviteGroup3(InviteGroup3Request request) {
        String randomUUID = request.randomUUID();
        String encByRandomUUID = request.encByRandomUUID();

        //randomkeyforredis redis에 TTL 설정해서 저장
        redisTemplate.opsForValue().set("INVITE_KEY:" +  randomUUID,encByRandomUUID, Duration.ofMinutes(120)); // 120분 유효

        return new InviteGroup3Response(randomUUID, "발급하신 초대코드가 redis에 저장되었습니다. (유효기한 : 120분)");
    }

//======================
// 그룹 관리 - 그룹 초대받기 (Step1,2)
//======================

    //그룹 관리 - 그룹 초대받기 - step1 - 메인 서비스 메소드
    @Transactional
    public JoinGroup1Response joinGroup1(JoinGroup1Request request,String userId) {
        String randomUUID = request.randomUUID();
        String storedUUID = redisTemplate.opsForValue().get("INVITE_KEY:" + randomUUID);
        if (storedUUID == null){
            throw new GroupInviteCodeExpired(BaseErrorCode.GROUP_INVITECODE_EXPIRED, "[ERROR]: 초대코드가 만료되거나 유효하지 않습니다.");
        }

        return new JoinGroup1Response(storedUUID);
    }


    //그룹 관리 - 그룹 초대받기 - step2 - 메인 서비스 메소드
    public JoinGroup2Response joinGroup2(JoinGroup2Request request, String userId) {

        //값 꺼내기
        String groupId = request.groupId(); //그룹 아이디
        String encGroupKey = request.encGroupKey(); //개인키로 암호화한 그룹키
        String encUserId = request.encUserId(); //그룹키로 암호화한 사용자 고유 아이디
        String encGroupId = request.encGroupId(); //개인키로 암호화한 그룹 아이디
        String encencGroupMemberId = request.encencGroupMemberId(); //개인키로 암호화한 encUserId

        //GroupProxyUser에 저장
        groupProxyUserRepository.save(GroupProxyUser.of(userId,encGroupId,encencGroupMemberId,System.currentTimeMillis()));

        //GroupShareKey에 저장
        groupShareKeyRepository.save(GroupShareKey.of(groupId, encUserId, encGroupKey));

        //그룹 이름
        Group joinedGroupName = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupIdNotFoundException(BaseErrorCode.GROUP_ID_NOTFOUND, "[ERROR]: 존재하지 않는 그룹입니다: " + groupId));
        return new JoinGroup2Response(joinedGroupName.getGroupName() + "그룹에 참여 완료했어요.");
    }
}
