package com.website.service.item.model;

import com.website.repository.item.model.ItemSearchSortType;
import com.website.repository.item.model.SearchItemCriteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemRequestDto {
    private Integer size;
    private ItemSearchSortType sortType;
    private String searchAfter;
    private boolean withTotalCount;
    private Long categoryId;
    private Long subcategoryId;
    private String searchName;
    private boolean onDiscount;

    public SearchItemCriteria toCriteria() {
        return SearchItemCriteria.builder()
                .size(size)
                .sortType(sortType)
                .searchAfter(searchAfter)
                .withTotalCount(withTotalCount)
                .categoryId(categoryId)
                .subcategoryId(subcategoryId)
                .searchName(searchName)
                .onDiscount(onDiscount)
                .build()
                ;
    }
}
