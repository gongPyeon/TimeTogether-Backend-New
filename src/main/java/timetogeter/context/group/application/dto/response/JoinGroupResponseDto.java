package timetogeter.context.group.application.dto.response;

public record JoinGroupResponseDto(
        String groupId,
        String groupName,
        String groupImg,
        String invitedId,
        Long groupPeopleNum
) {
}
