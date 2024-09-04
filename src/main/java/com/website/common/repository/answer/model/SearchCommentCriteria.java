package com.website.common.repository.answer.model;

import com.website.common.repository.comment.model.CommentSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchCommentCriteria {
    private Integer size;
    private CommentSortType sortType;
    private String nextSearchAfter;
    private boolean withTotalCount;

    //
    private Long categoryId;
    private Long subcategoryId;
    private Long userId;
    private  Long itemId;
    private boolean isNoneWithAnswer;
}
