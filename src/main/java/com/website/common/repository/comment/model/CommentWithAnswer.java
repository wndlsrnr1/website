package com.website.common.repository.comment.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class CommentWithAnswer {

    private Long id;

    private Long itemId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long answerId;

    private String answerContent;

    @QueryProjection
    public CommentWithAnswer(Long id, Long itemId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Long answerId, String answerContent) {
        this.id = id;
        this.itemId = itemId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }
}
