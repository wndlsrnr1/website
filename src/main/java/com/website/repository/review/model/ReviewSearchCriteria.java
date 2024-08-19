package com.website.repository.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSearchCriteria {
    private Long userId;
    private Long itemId;
    private int size;
    private ReviewSortType sortType;
    private boolean withTotalCount;
    private String nextSearchAfter;
}
