package com.website.customer.service.review.model;

import com.website.common.repository.review.model.ReviewSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSearchCriteria {
    private Long itemId;
    private Long userId;
    private String nextSearchAfter;
    private Integer size;
    private ReviewSortType sortType;
    private boolean withTotalCount;
}
