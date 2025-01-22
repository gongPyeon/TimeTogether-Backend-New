package com.pro.domain.group.presentation;

import com.pro.base.BaseResponse;
import com.pro.domain.group.application.GroupService;
import com.pro.domain.group.dto.response.GroupInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class GroupTestController {
  @Autowired
  private GroupService groupService;

  @GetMapping("/test")
  public BaseResponse<List<GroupInfoResponse>> groupInfo() {
    List<GroupInfoResponse> groupInfoResponseList = groupService.findManagerGroup("haerizian10@gmail.com");
    return new BaseResponse<>(groupInfoResponseList);
  }

}
