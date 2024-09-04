package com.website.common.repository.purchases.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasesSearchCriteria {
    private Long userId;
    private Long itemId;
    private OrderStatus status;
    private PurchasesSortType sort;
    private int size;
    private String searchAfter;
    private boolean withTotalCount;
}
