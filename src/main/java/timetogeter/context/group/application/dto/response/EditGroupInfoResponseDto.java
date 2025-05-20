package timetogeter.context.group.application.dto.response;

import java.util.List;

public record EditGroupInfoResponseDto(
        String groupId,
        String groupName,
        String groupImg,
        String managerId,
        //TODO: 약속 정하는 중 정보 후에 추가 예정
        //TODO: 약속 확정 완료 후에 추가 예정
        List<String> groupMembers
) {
}
