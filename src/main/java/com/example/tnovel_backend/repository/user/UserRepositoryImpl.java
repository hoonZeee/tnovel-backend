package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.controller.admin.dto.request.UserTotalSearchRequest;
import com.example.tnovel_backend.repository.user.entity.QUser;
import com.example.tnovel_backend.repository.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> searchUsers(UserTotalSearchRequest request, Pageable pageable) {
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            builder.and(user.username.containsIgnoreCase(request.getUsername()));
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            builder.and(user.name.containsIgnoreCase(request.getName()));
        }

        if (request.getCreatedDate() != null) {
            LocalDateTime start = request.getCreatedDate().atStartOfDay();
            LocalDateTime end = request.getCreatedDate().plusDays(1).atStartOfDay();
            builder.and(user.createdAt.between(start, end));
        }

        if (request.getStatus() != null) {
            builder.and(user.status.eq(request.getStatus()));
        }

        List<User> results = queryFactory
                .selectFrom(user)
                .where(builder)
                .orderBy(user.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(user.id.count())
                .from(user)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}

