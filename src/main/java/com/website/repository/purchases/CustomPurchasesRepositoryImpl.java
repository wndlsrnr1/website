package com.website.repository.purchases;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.common.PageResult;
import com.website.repository.purchases.model.*;
import com.website.utils.common.SearchAfterEncoder;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class CustomPurchasesRepositoryImpl extends QuerydslRepositorySupport implements CustomPurchasesRepository {

    private final QPurchases purchases = QPurchases.purchases;

    public CustomPurchasesRepositoryImpl() {
        super(Purchases.class);
    }

    @Override
    public PageResult<Purchases> search(PurchasesSearchCriteria criteria) {
        List<Purchases> purchasesList = from(purchases)
                .where(searchWhereCondition(criteria))
                .limit(criteria.getSize())
                .orderBy(order(criteria.getSort()))
                .fetch();

        String nextSearchAfter = getNextSearchAfter(criteria, purchasesList);

        return PageResult.<Purchases>builder()
                .items(purchasesList)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(criteria.isWithTotalCount() ? count(criteria) : null)
                .build();
    }

    private Long count(PurchasesSearchCriteria criteria) {
        return from(purchases).where(searchWhereCondition(criteria)).fetchCount();
    }

    private String getNextSearchAfter(PurchasesSearchCriteria criteria, List<Purchases> purchasesList) {
        String nextSearchAfter = null;
        if (purchasesList.size() == criteria.getSize()) {
            switch (criteria.getSort()) {
                case RECENT: {
                    Purchases lastPurchases = purchasesList.get(purchasesList.size() - 1);
                    nextSearchAfter = SearchAfterEncoder.encode(lastPurchases.getId().toString());
                    break;
                }
                case AMOUNT: {
                    Purchases lastPurchases = purchasesList.get(purchasesList.size() - 1);
                    String nextAmount = lastPurchases.getTotalAmount().toString();
                    String nextId = lastPurchases.getId().toString();
                    nextSearchAfter = SearchAfterEncoder.encode(nextId, nextAmount);
                    break;
                }
            }
        }
        return nextSearchAfter;
    }


    private OrderSpecifier<?>[] order(PurchasesSortType sortType) {
        switch (sortType) {
            case RECENT: {
                return new OrderSpecifier<?>[]{purchases.id.desc()};
            }
            case AMOUNT: {
                return new OrderSpecifier<?>[]{
                        new OrderSpecifier<>(Order.DESC, purchases.totalAmount),
                        new OrderSpecifier<>(Order.DESC, purchases.id)};
            }
            default:
                throw new ClientException(ErrorCode.INTERNAL_SERVER_ERROR,
                        "invalid sort type. sortType=" + sortType);
        }
    }

    private BooleanExpression searchWhereCondition(PurchasesSearchCriteria criteria) {
        return statusEqual(criteria.getStatus())
                .and(userIdEq(criteria.getUserId()))
                .and(nextPurchases(criteria));
    }

    private BooleanExpression statusEqual(OrderStatus status) {
        return status == null ? null : purchases.status.eq(status);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : purchases.user.id.eq(userId);
    }

    private BooleanExpression nextPurchases(PurchasesSearchCriteria criteria) {
        if (criteria.getSearchAfter() == null) {
            return null;
        }
        switch (criteria.getSort()) {
            case RECENT: {
                String decode = SearchAfterEncoder.decodeSingle(criteria.getSearchAfter());
                long lastPurchasesId = Long.parseLong(decode);
                return purchases.id.lt(lastPurchasesId);
            }
            case AMOUNT: {
                String[] decodes = SearchAfterEncoder.decode(criteria.getSearchAfter());
                long lastPurchasesId = Long.parseLong(decodes[0]);
                int lastAmount = Integer.parseInt(decodes[1]);

                return purchases.totalAmount.lt(lastAmount)
                        .or(
                                purchases.totalAmount.eq(lastAmount).and(purchases.id.lt(lastPurchasesId))
                        );
            }
            default:
                throw new ClientException(ErrorCode.BAD_REQUEST, "search after incorrect. criteria=" + criteria);
        }
    }


}
