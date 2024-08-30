package com.website.service.purchases.model;

import com.website.config.auth.ServiceUser;
import com.website.repository.purchases.model.OrderStatus;
import com.website.repository.purchases.model.PurchasesSearchCriteria;
import com.website.repository.purchases.model.PurchasesSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

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
