package timetogeter.context.schedule.application.dto.request;

import java.util.List;

public record GetPromiseBatchReqDTO (List<String> scheduleIdList,
                                     String pseudoId)
{}
