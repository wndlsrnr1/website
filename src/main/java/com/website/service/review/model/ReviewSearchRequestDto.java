package com.website.service.review.model;

import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.review.model.ReviewSearchCriteria;
import com.website.repository.review.model.ReviewSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSearchRequestDto {
    private Long userId;
    private Long itemId;
    private int size;
    private ReviewSortType sortType;
    private boolean withTotalCount;
    private String nextSearchAfter;

    public ReviewSearchCriteria toCriteria() {
        return ReviewSearchCriteria.builder()
                .userId(userId)
                .itemId(itemId)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .nextSearchAfter(nextSearchAfter)
                .build();
    }
}
