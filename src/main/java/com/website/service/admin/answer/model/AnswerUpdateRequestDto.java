package com.website.service.admin.answer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerUpdateRequestDto {
    private Long commentId;
    private String content;
}
