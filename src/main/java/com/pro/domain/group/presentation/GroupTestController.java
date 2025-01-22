package com.pro.domain.group.presentation;

import com.pro.base.BaseResponse;
import com.pro.base.constant.Code;
import com.pro.domain.group.application.GroupService;
import com.pro.domain.group.dto.response.GroupInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class GroupTestController {
  @Autowired
  private GroupService groupService;

  @GetMapping("/test")
  public ResponseEntity<BaseResponse<List<GroupInfoResponse>>> groupInfo(HttpServletRequest request) {
    log.info("[request] : {}",  request);
    List<GroupInfoResponse> groupInfoResponseList = groupService.findManagerGroup("aaa");
    return ResponseEntity.ok(new BaseResponse<>(groupInfoResponseList));
  }

}
