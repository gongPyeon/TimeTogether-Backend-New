package timetogeter.context.group.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    @Id
    private String groupId;

    private String groupName;
    private String groupImg;
    private String managerId; // userId

    Group(String groupName, String groupImg, String managerId) {
        this.groupId = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.managerId = managerId;
    }
}
