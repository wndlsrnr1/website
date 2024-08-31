package com.website.repository.answer.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class CommentSearch {
    private Long commentId;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;
    private Long itemId;
    private Long userId;

    @QueryProjection
    public CommentSearch(Long commentId, String commentContent, LocalDateTime commentCreatedAt, LocalDateTime commentUpdatedAt, Long itemId, Long userId) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
        this.itemId = itemId;
        this.userId = userId;
    }
}
