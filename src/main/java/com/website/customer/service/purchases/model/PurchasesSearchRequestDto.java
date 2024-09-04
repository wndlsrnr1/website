package com.website.customer.service.purchases.model;

import com.website.common.repository.purchases.model.OrderStatus;
import com.website.common.repository.purchases.model.PurchasesSearchCriteria;
import com.website.common.repository.purchases.model.PurchasesSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasesSearchRequestDto {
    private Long userId;
    private Long itemId;
    private OrderStatus orderStatus;
    private PurchasesSortType sortType;
    private Integer size;
    private String searchAfter;
    private boolean withTotalCount;


    public PurchasesSearchCriteria toCriteria() {
        return PurchasesSearchCriteria.builder()
                .userId(userId)
                .itemId(itemId)
                .status(orderStatus)
                .sort(sortType)
                .size(size)
                .searchAfter(searchAfter)
                .withTotalCount(withTotalCount)
                .build();
    }
}
