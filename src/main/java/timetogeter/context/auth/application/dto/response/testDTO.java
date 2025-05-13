package timetogeter.context.auth.application.dto.response;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Getter
public class testDTO {
    private String result;

    public testDTO(String result) {
        this.result = result;
    }
}
