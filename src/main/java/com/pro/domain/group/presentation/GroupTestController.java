package com.pro.domain.group.presentation;

import com.pro.base.BaseResponse;
import com.pro.base.constant.BaseResponseCode;
import com.pro.domain.group.application.GroupService;
import com.pro.domain.group.dto.response.GroupInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupTestController {
  @Autowired
  private GroupService groupService;
  @Autowired
  private HttpServletRequest request;

  @GetMapping("/test")
  public ResponseEntity<BaseResponse<List<GroupInfoResponse>>> groupInfo() {
    List<GroupInfoResponse> groupInfoResponseList = groupService.findManagerGroup("haerizian10@gmail.com");
    return ResponseEntity.ok(BaseResponse.of(BaseResponseCode.SUCCESS, groupInfoResponseList,request.getRequestURI()));
  }

}
