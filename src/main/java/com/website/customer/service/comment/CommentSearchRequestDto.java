package com.website.customer.service.comment;

import com.website.common.repository.comment.model.CommentSearchCriteria;
import com.website.common.repository.comment.model.CommentSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSearchRequestDto {
    private Long userId;
    private Long itemId;
    private int size;
    private CommentSortType sortType;
    private boolean withTotalCount;
    private String nextSearchAfter;

    public CommentSearchCriteria toCriteria() {
        return CommentSearchCriteria.builder()
                .userId(userId)
                .itemId(itemId)
                .size(size)
                .sortType(sortType)
                .withTotalCount(withTotalCount)
                .nextSearchAfter(nextSearchAfter)
                .build();
    }
}
