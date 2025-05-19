package timetogeter.context.auth.infrastructure.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import timetogeter.context.auth.domain.entity.QUser;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.custom.UserRepositoryCustom;
import timetogeter.context.auth.domain.vo.Provider;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<User> findByIdAndProviderType(String userId, Provider provider) {
        QUser user = QUser.user;

        User result = queryFactory
                .selectFrom(user)
                .where(
                        user.userId.eq(userId),
                        user.provider.eq(provider)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
