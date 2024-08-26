package com.website.repository.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSearchCriteria {
    private Long userId;
    private Long itemId;
    private int size;
    private CommentSortType sortType;
    private boolean withTotalCount;
    private String nextSearchAfter;
}