package timetogeter.context.time.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromiseCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promiseCheckId;

    private int dateId;
    private int promiseId;
    private boolean isConfirmed;
}
