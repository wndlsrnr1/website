package com.website.controller.api.comment.model;

import com.website.service.comment.model.CommentWithAnswerDto;
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

    public static CommentWithAnswerResponse of(CommentWithAnswerDto dto) {
        return CommentWithAnswerResponse.builder()
                .id(dto.getId())
                .itemId(dto.getId())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .answerId(dto.getAnswerId())
                .build();
    }
}
