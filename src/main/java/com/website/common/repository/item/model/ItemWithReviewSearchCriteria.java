package com.website.common.repository.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithReviewSearchCriteria {
    private String nextSearchAfter;
    private Integer size;
    private ItemWithReviewSortType sortType;
    private boolean withTotalCount;
}
