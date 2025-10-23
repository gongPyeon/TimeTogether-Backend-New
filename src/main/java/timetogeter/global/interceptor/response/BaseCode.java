package timetogeter.global.interceptor.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import timetogeter.global.interceptor.response.StatusCode;

@Getter
public enum BaseCode implements StatusCode {
    OK("요청에 성공했습니다."),
    SUCCESS_SIGN_UP("회원가입에 성공했어요"),
    SUCCESS_ID("사용 가능한 아이디에요"),
    SUCCESS_REISSUE("액세스 토큰이 재발급됐어요"),
    SUCCESS_LOGIN("로그인에 성공했어요"),
    SUCCESS_LOGOUT("로그아웃에 성공했어요"),
    SUCCESS_DELETE("등록한 장소를 삭제했어요"), 
    SUCCESS_VOTE("투표했어요"),
    SUCCESS_DELETE_VOTE("투표를 취소했어요"),
    SUCCESS_REGISTER_PLACE("장소 등록에 성공했어요"),
    SUCCESS_CONFIRM_PLACE("장소를 확정했어요"),
    SUCCESS_SCHEDULE_UPDATE("일정 등록에 성공했어요"),
    SUCCESS_EXIT_PROMISE("약속을 나갔어요"),
    TIME_OK("내 시간을 저장했어요"), // TODO: 기간 확인
    SUCCESS_RECOMMEND_PLACE("추천을 완료했어요"),
    SUCCESS_PLACE_RATING("별점이 등록되었어요 \n 다음에 더 똑똑하게 추천해 드릴게요"),
    SUCCESS_TRAIN_PLACE("장소 학습을 완료했어요");

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    BaseCode(String message) {
        this.code = 200;
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }
}
