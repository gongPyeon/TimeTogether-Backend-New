package timetogeter.context.group.application.dto.request;

public record CreateGroup1Request(
        String groupName,
        String groupExplain,
        String groupImg
        //String explain (중복되어 주석처리)
) {}
