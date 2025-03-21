package com.pro.domain.group.repository;

import com.pro.domain.group.dto.response.GroupInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pro.domain.group.domain.QGroup.group;

@Repository
@RequiredArgsConstructor
public class GroupCustomRepositoryImpl implements GroupCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<GroupInfoResponse> findByGroupManager(String email) {
    return jpaQueryFactory.select(
                    Projections.constructor(GroupInfoResponse.class, group.name, group.img,
                            group.managerEmail)
            )
            .from(group)
            .where()
            .fetch();
  }
}
