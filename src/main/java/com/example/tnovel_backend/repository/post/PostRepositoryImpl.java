package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.controller.admin.dto.request.PostSearchRequestDto;
import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.post.entity.QPost;
import com.example.tnovel_backend.repository.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllOrderedByCreatedAt(Pageable pageable) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        List<Post> results = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }


    @Override
    public Page<Post> searchPost(PostSearchRequestDto request, Pageable pageable) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            builder.and(user.username.containsIgnoreCase(request.getUsername()));
        }

        if (request.getCreatedDate() != null) {
            LocalDateTime start = request.getCreatedDate().atStartOfDay();
            LocalDateTime end = request.getCreatedDate().plusDays(1).atStartOfDay();
            builder.and(post.createdAt.between(start, end));
        }

        if (request.getVisibleStatus() != null) {
            builder.and(post.visibleStatus.eq(request.getVisibleStatus()));
        }

        List<Post> results = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(builder)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .join(post.user, user)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
