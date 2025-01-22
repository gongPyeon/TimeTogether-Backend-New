package com.pro.domain.group.dto.response;


import com.pro.domain.group.domain.Group;
import com.querydsl.core.annotations.QueryProjection;

public record GroupInfoResponse(
        String name,
        String img,
        String managerEmail
) {
  @QueryProjection
  public GroupInfoResponse(String name, String img, String managerEmail) {
    this.name = name;
    this.img = img;
    this.managerEmail = managerEmail;
  }

  public static GroupInfoResponse from(Group group) {
    return new GroupInfoResponse(
            group.getName(),
            group.getImg(),
            group.getManagerEmail()
    );
  }

}
