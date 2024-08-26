package com.website.service.item.model;

import com.website.repository.item.model.ItemWithReviewSearchCriteria;
import com.website.repository.item.model.ItemWithReviewSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithReviewSearchRequestDto {
    private String nextSearchAfter;
    private Integer size;
    private ItemWithReviewSortType sortType;
    private boolean withTotalCount;

    public ItemWithReviewSearchCriteria toCriteria() {
        return ItemWithReviewSearchCriteria.builder()
                .nextSearchAfter(nextSearchAfter)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .build();
    }
}
