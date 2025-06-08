package timetogeter.context.promise.application.dto.request.manage;

import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDate;

public record CreatePromiseAlimRequest3(
        String groupId,
        String title,
        PromiseType type,

        String promiseImg,
        String managerId, // userId

        LocalDate startDate,
        LocalDate endDate


) {
}
