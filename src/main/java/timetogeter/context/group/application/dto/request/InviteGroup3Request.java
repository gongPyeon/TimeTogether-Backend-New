package timetogeter.context.group.application.dto.request;

public record InviteGroup3Request(
        String randomKeyForRedis,
        String s3reserve //[그룹키 + groupId(그룹아이디) + userId(이 대상을 위한 초대코드임을 의미)] 랜덤UUID 로 암호화한 값
) {
}
