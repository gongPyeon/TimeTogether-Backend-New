package timetogeter.context.promise.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogeter.context.promise.domain.vo.PromiseType;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromiseCheck {
    @Id
    private Long promiseCheckId;

    private Long dateId;
    private String promiseId;
    private boolean isConfirmed;

    private PromiseCheck(Long dateId, String promiseId, boolean isConfirmed) {
        this.dateId = dateId;
        this.promiseId = promiseId;
        this.isConfirmed = isConfirmed;
    }

    public static PromiseCheck of(Long dateId, String promiseId, boolean isConfirmed) {
        return new PromiseCheck(dateId, promiseId,isConfirmed);
    }

}
