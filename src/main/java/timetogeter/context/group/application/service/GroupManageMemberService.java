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
import timetogeter.context.group.application.dto.response.InviteGroup1Response;
import timetogeter.context.group.application.dto.response.InviteGroup2Response;
import timetogeter.context.group.application.dto.response.JoinGroup0Response;
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
    public String inviteGroup3(InviteGroup3Request request) {
        String validInviteCodeCheck = request.randomKeyForRedis();
        String s3reserve = request.s3reserve();


        //randomkeyforredis redis에 TTL 설정해서 저장
        redisTemplate.opsForValue().set("INVITE_KEY:" + validInviteCodeCheck, validInviteCodeCheck, Duration.ofMinutes(60)); // 60분 유효

        //s3reserve를 s3 버킷에 randomkeyforredis와 함께 저장
        StringBuilder sb = new StringBuilder();
        try {
            if (amazonS3.doesObjectExist(bucketName, s3Key)) {
                S3Object s3Object = amazonS3.getObject(bucketName, s3Key);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
            }
            //새로운 매핑 추가
            sb.append(validInviteCodeCheck).append(" : ").append(s3reserve).append("\n");

            //S3에 다시 업로드
            File tempFile = File.createTempFile("invite_map", ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, StandardCharsets.UTF_8))) {
                writer.write(sb.toString());
            }

            amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, tempFile));
            tempFile.deleteOnExit();

        } catch (IOException e) {
            throw new RuntimeException("S3 처리 중 오류 발생", e);
        }

        //(방향성 설명) 초대 수락시 redis에서 유효한 TTL 값인지 확인후, randomkeyforredis로 s3reserve를 반환할 예정

        return "발급하신 초대코드의 유효기한 : 60분";
    }

//======================
// 그룹 관리 - 그룹 초대받기 (Step1)
//======================

    //그룹 관리 - 그룹 초대받기 - step0 - 메인 서비스 메소드
    @Transactional
    public JoinGroup0Response joinGroup0(JoinGroup0Request request, String userId) {
        String objectKey = request.randomKeyForRedis();

        if (!amazonS3.doesObjectExist(bucketName, objectKey)) {
            return new JoinGroup0Response("false");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = new HashMap<>();
        payload.put("randomKey", objectKey);
        payload.put("userId", userId);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(

                    "will update",//lambdaVerifyUrl,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            String decryptedS3reserve = response.getBody();
            return new JoinGroup0Response(decryptedS3reserve);

        } catch (Exception e) {
            return new JoinGroup0Response("false");
        }
    }

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
