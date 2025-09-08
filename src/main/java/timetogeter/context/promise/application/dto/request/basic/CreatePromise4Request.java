package timetogeter.context.promise.application.dto.request.basic;

import java.time.LocalDate;

public record CreatePromise4Request(
        String groupId,
        String title,
        String type, //스터디 or 식사

        String promiseImg,
        String managerId, //userId 약속장

        LocalDate startDate,
        LocalDate endDate
) {
}
