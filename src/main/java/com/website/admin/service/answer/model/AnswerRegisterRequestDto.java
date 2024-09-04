package com.website.admin.service.answer.model;

import com.website.common.repository.answer.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnswerRegisterRequestDto {
    private Long commentId;
    private String content;

    public static Answer toEntity(Long createdBy, AnswerRegisterRequestDto dto) {
        return Answer.builder()
                .commentId(dto.getCommentId())
                .createdBy(createdBy)
                .content(dto.getContent())
                .build();
    }
}
