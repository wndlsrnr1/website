package com.website.customer.controller.comment.model;

import com.website.customer.service.comment.CommentWithAnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentWithAnswerResponse {

    private Long id;
    private Long itemId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long answerId;
    private String answerContent;

    public static CommentWithAnswerResponse of(CommentWithAnswerDto dto) {
        return CommentWithAnswerResponse.builder()
                .id(dto.getId())
                .itemId(dto.getItemId())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .answerId(dto.getAnswerId())
                .answerContent(dto.getAnswerContent())
                .build();
    }
}
