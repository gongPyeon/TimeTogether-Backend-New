package com.pro.domain.group.application;

import com.pro.domain.group.dto.response.GroupInfoResponse;
import com.pro.domain.group.repository.GroupCustomRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

  @Autowired
  private GroupCustomRepositoryImpl groupCustomRepository;

  public List<GroupInfoResponse> findManagerGroup(String email) {
    return groupCustomRepository.findByGroupManager(email);
  }
}
