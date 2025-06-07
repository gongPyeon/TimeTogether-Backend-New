package timetogeter.context.group.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogeter.context.group.application.exception.GroupNotFoundException;
import timetogeter.context.group.domain.entity.Group;
import timetogeter.context.group.domain.repository.GroupRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupInfoService {

    private final GroupRepository groupRepository;

    public String getGroupName(String groupId){
        Group group = get(groupId);
        return group.getGroupName();
    }

    private Group get(String groupId){
        return groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new GroupNotFoundException(BaseErrorCode.GROUP_NOT_FOUND, "[ERROR]: " + groupId + "에 해당하는 그룹이 존재하지 않습니다."));
    }
}
