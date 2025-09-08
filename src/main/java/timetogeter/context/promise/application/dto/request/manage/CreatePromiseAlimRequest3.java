package timetogeter.context.promise.application.dto.request.manage;

import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreatePromiseAlimRequest3(
        String groupId,
        String title,
        String type,

        String promiseImg,
        String managerId, // userId

        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
