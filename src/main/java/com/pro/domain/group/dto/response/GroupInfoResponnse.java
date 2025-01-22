package com.pro.domain.group.dto.response;


import com.pro.domain.group.domain.Group;
import com.querydsl.core.annotations.QueryProjection;

public record GroupInfoResponnse(
        String name,
        String img,
        String managerEmail
) {
  @QueryProjection
  public GroupInfoResponnse(String name, String img, String managerEmail) {
    this.name = name;
    this.img = img;
    this.managerEmail = managerEmail;
  }

  public static GroupInfoResponnse from(Group group) {
    return new GroupInfoResponnse(
            group.getName(),
            group.getImg(),
            group.getManagerEmail()
    );
  }

}
