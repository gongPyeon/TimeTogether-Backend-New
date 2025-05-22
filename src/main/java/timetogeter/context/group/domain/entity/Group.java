package timetogeter.context.group.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "group_table")
public class Group {
    @Id
    private String groupId;

    private String groupName;
    private String groupImg;
    private String explain;
    private String managerId; // userId

    private Group(String groupName, String groupImg, String explain, String managerId) {
        this.groupId = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.explain = explain;
        this.groupImg = groupImg;
        this.managerId = managerId;
    }

    public static Group of(String groupName, String groupImg, String explain, String managerId) {
        return new Group(groupName, groupImg, explain, managerId);
    }

    //=======================================================================

    //업데이트 로직
    public void updateName(String newName) {
        this.groupName = newName;
    }

    public void updateImg(String img) {
        this.groupImg = img;
    }
}
