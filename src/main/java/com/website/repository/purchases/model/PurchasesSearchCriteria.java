package com.website.repository.purchases.model;

import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.PurchasesSortType;
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
