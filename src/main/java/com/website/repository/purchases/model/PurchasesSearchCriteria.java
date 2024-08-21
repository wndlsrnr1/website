package com.website.repository.purchases.model;

import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.PurchasesSortType;
import lombok.Data;

@Data
public class PurchasesSearchCriteria {
    private Long userId;
    private OrderStatus status;
    private PurchasesSortType sort;
    private int size;
    private String searchAfter;
    private boolean withTotalCount;
}
