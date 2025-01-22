package com.pro.domain.group.repository;

import com.pro.domain.group.dto.response.GroupInfoResponse;

import java.util.List;

public interface GroupCustomRepository {
  List<GroupInfoResponse> findByGroupManager(String email);
}
