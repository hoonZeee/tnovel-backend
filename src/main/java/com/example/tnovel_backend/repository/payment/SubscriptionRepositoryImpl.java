package com.example.tnovel_backend.repository.payment;

import com.example.tnovel_backend.controller.admin.dto.request.SubscriptionSearchRequest;
import com.example.tnovel_backend.repository.payment.entity.QSubscription;
import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
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
public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Subscription> findAllOrderedByLatestActivity(Pageable pageable) {
        QSubscription subscription = QSubscription.subscription;
        QUser user = QUser.user;

        List<Subscription> results = queryFactory
                .selectFrom(subscription)
                .join(subscription.user, user).fetchJoin()
                .orderBy(
                        subscription.canceledAt.coalesce(subscription.startedAt).desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }


    @Override
    public Page<Subscription> searchSubscription(SubscriptionSearchRequest request, Pageable pageable) {
        QSubscription subscription = QSubscription.subscription;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            builder.and(user.username.containsIgnoreCase(request.getUsername()));
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            builder.and(user.name.containsIgnoreCase(request.getName()));
        }

        if (request.getStartedDate() != null) {
            LocalDateTime start = request.getStartedDate().atStartOfDay();
            LocalDateTime end = request.getStartedDate().plusDays(1).atStartOfDay();
            builder.and(subscription.startedAt.between(start, end));
        }

        if (request.getCanceledDate() != null) {
            builder.and(subscription.subscribeStatus.eq(SubscribeStatus.CANCELED));
        }

        if (request.getSubscribeStatus() != null) {
            builder.and(subscription.subscribeStatus.eq(request.getSubscribeStatus()));
        }

        List<Subscription> results = queryFactory
                .selectFrom(subscription)
                .join(subscription.user, user).fetchJoin()
                .where(builder)
                .orderBy(subscription.startedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .join(subscription.user, user)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
