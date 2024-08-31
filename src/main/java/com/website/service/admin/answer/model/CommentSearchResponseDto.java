package com.website.service.admin.answer.model;

import com.website.controller.admin.answer.model.CommentSearchResponse;
import com.website.repository.answer.model.CommentSearch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSearchResponseDto {
    private Long commentId;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;
    private Long itemId;
    private Long userId;

    public static CommentSearchResponseDto of(CommentSearch fetchResult) {
        return CommentSearchResponseDto.builder()
                .commentId(fetchResult.getCommentId())
                .commentContent(fetchResult.getCommentContent())
                .commentCreatedAt(fetchResult.getCommentCreatedAt())
                .commentUpdatedAt(fetchResult.getCommentUpdatedAt())
                .itemId(fetchResult.getItemId())
                .userId(fetchResult.getUserId())
                .build();
    }
}
