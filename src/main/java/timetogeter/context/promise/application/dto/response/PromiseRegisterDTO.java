package timetogeter.context.promise.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import timetogeter.context.promise.domain.vo.PromiseType;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PromiseRegisterDTO {
    private String dateTime;
    private String title;
    private PromiseType type;
    private String place;
    private String placeUrl;
}

