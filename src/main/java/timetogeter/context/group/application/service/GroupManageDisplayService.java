package timetogeter.context.group.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.dto.request.ViewGroupsInRequestDto;
import timetogeter.context.group.application.dto.response.ViewGroupInfoDto;
import timetogeter.context.group.application.dto.response.ViewGroupsInResponseDto;
import timetogeter.context.group.application.exception.GroupIdDecryptException;
import timetogeter.context.group.domain.entity.GroupProxyUser;
import timetogeter.context.group.domain.repository.GroupProxyUserRepository;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.context.group.domain.repository.GroupShareKeyRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GroupManageDisplayService {
    private final GroupRepository groupRepository;
    private final GroupProxyUserRepository groupProxyUserRepository;
    private final GroupShareKeyRepository groupShareKeyRepository;

    //그룹메인보기
    public List<ViewGroupsInResponseDto> viewGroupsIn(ViewGroupsInRequestDto request, String userId) {
        //0. 개인 마스터키 추출
        String masterKey = request.personalMasterKey();

        //1. 사용자 고유 아이디(userId)로 '개인키로 암호화한 그룹 아이디'(encGroupId) 찾기
        List<String> encGroupIds = groupProxyUserRepository.findEncGroupIdsByUserId(userId);

        //2. 개인키로 '개인키로 암호화한 그룹 아이디' 복호화 하기
        List<String> groupIdList = decryptEncGroupIdList(encGroupIds, masterKey);

        //3. 복호화한 그룹 아이디로 그룹 정보 반환 -- <1>
        List<ViewGroupInfoDto> groupInfoList = getViewGroupInfoDtoList(groupIdList);

        //4. 개인키로 '암호화한 그룹키'(encGroupKey) 복호화 하기
        List<String> groupKeyList = getGroupKeyList(groupIdList ,masterKey);

        //5. 그룹 아이디로 '그룹키로 암호화한 사용자 고유 아이디'(encUserId) 리스트 찾기
        List<List<String>> encUserIdListPerGroup = getEncUserIdListPerGroup(groupIdList);

        //6. 그룹키로 '사용자 고유 아이디' 복호화 하여 리스트 찾기 -- <2>
        List<List<String>> decryptUserIdListPerGroup = getDecryptUserIdListPerGroup(encUserIdListPerGroup, groupKeyList);

        //7. <1>, <2> 모두 List<ViewGroupsInResponseDto>에 넣어서 반환
        return listofViewGroupsIn(groupInfoList, decryptUserIdListPerGroup);
    }

    //7
    private List<ViewGroupsInResponseDto> listofViewGroupsIn(
            List<ViewGroupInfoDto> groupInfoList,
            List<List<String>> decryptedUserIdsPerGroup
    ) {
        int size = Math.min(groupInfoList.size(), decryptedUserIdsPerGroup.size());

        List<ViewGroupsInResponseDto> responseList = new java.util.ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            ViewGroupInfoDto info = groupInfoList.get(i);
            List<String> members = decryptedUserIdsPerGroup.get(i);

            responseList.add(new ViewGroupsInResponseDto(
                    info.groupName(),
                    info.groupImg(),
                    members
            ));
        }

        return responseList;
    }

    //6
    private List<List<String>> getDecryptUserIdListPerGroup(List<List<String>> encUserIdsPerGroup, List<String> groupKeyList) {
        int size = Math.min(encUserIdsPerGroup.size(), groupKeyList.size());

        List<List<String>> decryptedUserIdsPerGroup = new java.util.ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            final int index = i;
            List<String> encUserIds = encUserIdsPerGroup.get(i);
            String groupKey = groupKeyList.get(i);

            List<String> decryptedUserIds = encUserIds.stream()
                    .map(encUserId -> {
                        try {
                            return EncryptUtil.decryptAESGCM(encUserId, groupKey);
                        } catch (Exception e) {
                            log.error("encUserId 복호화 실패: groupKeyIdx={}, encUserId={}, error={}", index, encUserId, e.getMessage());
                            return null;
                        }
                    })
                    .filter(decrypted -> decrypted != null)
                    .collect(Collectors.toList());

            decryptedUserIdsPerGroup.add(decryptedUserIds);
        }

        return decryptedUserIdsPerGroup;
    }

    //5
    private List<List<String>> getEncUserIdListPerGroup(List<String> groupIdList) {
        return groupIdList.stream()
                .map(groupId -> {
                    List<String> encUserIds = groupShareKeyRepository.findEncUserIdsByGroupId(groupId);
                    if (encUserIds == null) {
                        log.warn("groupId={}에 대한 encUserId 리스트 없음", groupId);
                        return List.<String>of();
                    }
                    return encUserIds;
                })
                .collect(Collectors.toList());
    }

    //4
    // 4
    private List<String> getGroupKeyList(List<String> groupIdList, String masterKey) {
        return groupIdList.stream()
                .map(groupId -> {
                    try {
                        // 암호화된 그룹키들 조회
                        List<String> encGroupKeys = groupShareKeyRepository.findEncGroupKeyByGroupId(groupId);
                        if (encGroupKeys == null || encGroupKeys.isEmpty()) {
                            log.warn("GroupId={}에 해당하는 암호화된 그룹키가 존재하지 않음", groupId);
                            return null;
                        }

                        // 복호화 가능한 첫 번째 그룹키 반환
                        return encGroupKeys.stream()
                                .map(encKey -> {
                                    try {
                                        return EncryptUtil.decryptAESGCM(encKey, masterKey);
                                    } catch (Exception e) {
                                        return null; // 복호화 실패한 것은 건너뜀
                                    }
                                })
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElseThrow(() -> {
                                    log.error("GroupId={}의 어떤 그룹키도 복호화할 수 없음", groupId);
                                    return new GroupIdDecryptException(BaseErrorCode.GROUP_KEY_DECRYPT_ERROR, "[ERROR] 복호화 가능한 그룹키 없음");
                                });

                    } catch (GroupIdDecryptException e) {
                        throw e;
                    } catch (Exception e) {
                        log.error("그룹키 복호화 중 예외 발생: groupId={}, error={}", groupId, e.getMessage());
                        throw new GroupIdDecryptException(BaseErrorCode.GROUP_KEY_DECRYPT_ERROR, "[ERROR] 암호화된 그룹키 복호화 실패");
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    //3
    private List<ViewGroupInfoDto> getViewGroupInfoDtoList(List<String> groupIds) {
        return groupRepository.findAllById(groupIds).stream()
                .map(group -> new ViewGroupInfoDto(
                        group.getGroupId(),
                        group.getGroupName(),
                        group.getGroupImg(),
                        group.getManagerId()
                ))
                .collect(Collectors.toList());
    }

    //2
    private static List<String> decryptEncGroupIdList(List<String> encGroupIds, String masterKey) {
        List<String> groupIds = encGroupIds.stream()
                .map(encGroupId -> {
                    try {
                        return EncryptUtil.decryptAESGCM(encGroupId, masterKey);
                    } catch (Exception e) {
                        log.error("복호화 실패: encGroupId={}, error={}", encGroupId, e.getMessage());
                        throw new GroupIdDecryptException(BaseErrorCode.GROUP_ID_DECRYPT_ERROR, "[ERROR] 암호화된 그룹 아이디 복호화 실패");
                    }
                })
                .filter(decrypted -> decrypted != null)
                .collect(Collectors.toList());
        return groupIds;
    }
}
