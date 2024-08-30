package com.website.repository.purchases;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.common.PageResult;
import com.website.repository.model.item.QItem;
import com.website.repository.model.item.QItemThumbnail;
import com.website.repository.purchases.model.*;
import com.website.repository.review.model.QReview;
import com.website.repository.user.model.QUser;
import com.website.utils.common.SearchAfterEncoder;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.website.repository.model.item.QItem.item;
import static com.website.repository.model.item.QItemThumbnail.itemThumbnail;
import static com.website.repository.purchases.model.QPurchases.purchases;
import static com.website.repository.review.model.QReview.review;
import static com.website.repository.user.model.QUser.user;


public class CustomPurchasesRepositoryImpl implements CustomPurchasesRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public CustomPurchasesRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public PageResult<PurchasesSearch> search(PurchasesSearchCriteria criteria) {
        List<PurchasesSearch> result = query.select(new QPurchasesSearch(
                        purchases.id,
                        user.id,
                        user.email,
                        review.id,
                        purchases.orderNumber,
                        purchases.orderDate,
                        item.id,
                        item.nameKor,
                        itemThumbnail.attachment.id,
                        purchases.status,
                        purchases.totalAmount,
                        purchases.address,
                        purchases.discount,
                        purchases.createdAt,
                        purchases.updatedAt
                )).from(purchases)
                .where(combineArray(searchWhereCondition(criteria), searchWhereConditionBySortType(criteria)))
                .join(item).on(purchases.item.id.eq(item.id))
                .join(itemThumbnail).on(item.id.eq(itemThumbnail.item.id))
                .join(user).on(purchases.user.id.eq(user.id))
                .leftJoin(review).on(purchases.id.eq(review.purchases.id))
                .orderBy(orderBySortType(criteria.getSort()))
                .limit(criteria.getSize())
                .fetch();


        String nextSearchAfter = getNextSearchAfter(criteria, result);

        Long totalCount = getTotalCount(criteria);

        return PageResult.<PurchasesSearch>builder()
                .items(result)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(criteria.isWithTotalCount() ? totalCount : null)
                .build();
    }

    private Long getTotalCount(PurchasesSearchCriteria criteria) {
        if (!criteria.isWithTotalCount()) {
            return null;
        }

        return query.selectFrom(purchases).where(
                searchWhereCondition(criteria)
        ).fetchCount();
    }

    private OrderSpecifier<?>[] orderBySortType(PurchasesSortType sort) {
        switch (sort) {
            case RECENT: {
                return new OrderSpecifier[]{
                        purchases.id.desc()
                };
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + sort.name());
            }
        }
    }

    private BooleanExpression[] combineArray(BooleanExpression[] whereClauseArray, BooleanExpression whereClause) {
        BooleanExpression[] whereConditions = Arrays.copyOf(whereClauseArray, whereClauseArray.length + 1);
        whereConditions[whereConditions.length - 1] = whereClause;
        return whereConditions;
    }

    private BooleanExpression searchWhereConditionBySortType(PurchasesSearchCriteria criteria) {
        if (criteria.getSearchAfter() == null) {
            return null;
        }
        switch (criteria.getSort()) {
            case RECENT: {
                String decoded = SearchAfterEncoder.decodeSingle(criteria.getSearchAfter());
                Long orderId = Long.parseLong(decoded);
                return purchases.id.lt(orderId);
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSort().name());
            }
        }
    }

    private String getNextSearchAfter(PurchasesSearchCriteria criteria, List<PurchasesSearch> purchasesList) {
        String nextSearchAfter = null;
        if (purchasesList.size() == criteria.getSize()) {
            switch (criteria.getSort()) {
                case RECENT: {
                    PurchasesSearch lastPurchases = purchasesList.get(purchasesList.size() - 1);
                    nextSearchAfter = SearchAfterEncoder.encode(lastPurchases.getId().toString());
                    break;
                }
            }
        }
        return nextSearchAfter;
    }


    private BooleanExpression[] searchWhereCondition(PurchasesSearchCriteria criteria) {
        return new BooleanExpression[]{
                userIdEq(criteria.getUserId()),
                itemIdEq(criteria.getItemId()),
                statusEqual(criteria.getStatus()),
        };
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? item.id.eq(itemId) : null;
    }

    private BooleanExpression statusEqual(OrderStatus status) {
        return status == null ? null : purchases.status.eq(status);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : purchases.user.id.eq(userId);
    }

}
