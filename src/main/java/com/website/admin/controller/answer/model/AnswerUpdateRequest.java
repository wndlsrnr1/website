package com.website.admin.controller.answer.model;

import com.website.admin.service.answer.model.AnswerUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnswerUpdateRequest {

    @NotBlank
    @Length(min = 8, max = 200)
    private String content;

    public AnswerUpdateRequestDto toDto(Long commentId, AnswerUpdateRequest request) {
        return AnswerUpdateRequestDto.builder()
                .commentId(commentId)
                .content(request.getContent())
                .build();
    }
}
