package timetogeter.context.group.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogeter.context.group.application.dto.request.EditGroup1Request;

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
    private String groupInfo;
    private String managerId; // userId

    private Group(String groupName, String groupImg, String description, String managerId) {
        this.groupId = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.groupInfo = description;
        this.groupImg = groupImg;
        this.managerId = managerId;
    }

    public static Group of(String groupName, String groupImg, String description, String managerId) {
        return new Group(groupName, groupImg, description, managerId);
    }

    //=======================================================================

    //업데이트 로직
    public void update(EditGroup1Request req){
        updateName(req.groupName());
        updateExplain(req.description());
        updateImg(req.groupImg());
    }
    public void updateName(String name) {
        if (name != null)
            this.groupName = name;
    }

    public void updateExplain(String groupInfo){
        if (groupInfo != null)
            this.groupInfo = groupInfo;
    }

    public void updateImg(String img) {
        if (img != null)
            this.groupImg = img;
    }
}
