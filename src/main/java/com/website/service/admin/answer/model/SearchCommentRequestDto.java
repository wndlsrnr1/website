package com.website.service.admin.answer.model;

import com.website.repository.answer.model.SearchCommentCriteria;
import com.website.repository.comment.model.CommentSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCommentRequestDto {
    private Integer size;
    private CommentSortType sortType;
    private String nextSearchAfter;
    private boolean withTotalCount;
    private Long categoryId;
    private Long subcategoryId;
    private Long userId;
    private  Long itemId;
    private boolean isNoneWithAnswer;

    public SearchCommentCriteria toCriteria() {
        return SearchCommentCriteria.builder()
                .size(size)
                .sortType(sortType)
                .nextSearchAfter(nextSearchAfter)
                .withTotalCount(withTotalCount)
                .categoryId(categoryId)
                .subcategoryId(subcategoryId)
                .userId(userId)
                .itemId(itemId)
                .isNoneWithAnswer(isNoneWithAnswer)
                .build();
    }
}
