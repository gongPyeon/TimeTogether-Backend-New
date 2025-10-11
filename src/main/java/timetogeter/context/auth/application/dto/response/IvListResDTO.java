package timetogeter.context.auth.application.dto.response;

import lombok.Getter;

@Getter
public class IvListResDTO {
    private String imgIv;
    private String emailIv;
    private String phoneIv;

    public IvListResDTO(String imgIv, String emailIv, String phoneIv) {
        this.imgIv = imgIv;
        this.emailIv = emailIv;
        this.phoneIv = phoneIv;
    }
}
