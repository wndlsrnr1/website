package com.website.common.repository.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSearchCriteria {
    private int size;
    private CommentSortType sortType;
    private boolean withTotalCount;
    private String nextSearchAfter;
    private Long userId;
    private Long itemId;
}
