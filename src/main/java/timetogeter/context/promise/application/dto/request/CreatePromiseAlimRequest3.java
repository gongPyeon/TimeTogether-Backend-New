package timetogeter.context.promise.application.dto.request;

import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDate;

public record CreatePromiseAlimRequest3(
        String groupId,
        String title,
        PromiseType type,

        String promiseImg,
        String encManagerId, // userId

        LocalDate startDate,
        LocalDate endDate


) {
}
