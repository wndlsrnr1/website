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
    private User user;
    private Item item;
    private int size;
    private ReviewSortType sortType;
    private boolean withTotalCount;
    private String nextSearchAfter;

    public ReviewSearchCriteria toCriteria() {
        return ReviewSearchCriteria.builder()
                .user(user)
                .item(item)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .nextSearchAfter(nextSearchAfter)
                .build();
    }
}
