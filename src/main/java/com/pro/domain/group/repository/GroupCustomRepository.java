package com.pro.domain.group.repository;

import com.pro.domain.group.domain.Group;
import com.pro.domain.group.dto.response.GroupInfoResponnse;

import java.util.List;

public interface GroupCustomRepository {
  List<GroupInfoResponnse> findByGroupManager(String email);
}
