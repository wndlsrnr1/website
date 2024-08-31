package com.website.controller.admin.answer.model;

import com.website.service.admin.answer.model.AnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerResponse {
    private Long id;
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnswerResponse of(AnswerDto dto) {
        return AnswerResponse.builder()
                .id(dto.getId())
                .commentId(dto.getCommentId())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
