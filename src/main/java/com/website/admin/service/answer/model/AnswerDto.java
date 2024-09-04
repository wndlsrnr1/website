package com.website.admin.service.answer.model;

import com.website.common.repository.answer.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerDto {

    private Long id;
    private Long commentId;
    private Long createdBy;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnswerDto of(Answer savedAnswer) {
        return AnswerDto.builder()
                .id(savedAnswer.getId())
                .commentId(savedAnswer.getCommentId())
                .createdBy(savedAnswer.getCreatedBy())
                .content(savedAnswer.getContent())
                .createdAt(savedAnswer.getCreatedAt())
                .updatedAt(savedAnswer.getUpdatedAt())
                .build();
    }
}
