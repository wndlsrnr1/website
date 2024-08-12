package com.website.repository.review.model;

import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSearchCriteria {
    private User user;
    private Item item;
    private int size;
    private ReviewSortType sortType;
    private boolean withTotalCount;
    private String nextSearchAfter;
}
