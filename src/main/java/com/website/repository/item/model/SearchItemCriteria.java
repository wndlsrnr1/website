package com.website.repository.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemCriteria {
    private Integer size;
    private ItemSearchSortType sortType;
    private String searchAfter;
    private boolean withTotalCount;
    private Long categoryId;
    private Long subcategoryId;
    private String searchName;
    private boolean onDiscount;
}
