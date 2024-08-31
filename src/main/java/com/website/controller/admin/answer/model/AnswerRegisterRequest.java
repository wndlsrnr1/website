package com.website.controller.admin.answer.model;

import com.website.service.admin.answer.model.AnswerRegisterRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerRegisterRequest {
    @NotBlank
    @Length(min = 8, max = 200)
    private String content;


    public AnswerRegisterRequestDto toDto(Long commentId) {
        return AnswerRegisterRequestDto.builder()
                .commentId(commentId)
                .content(content)
                .build();
    }
}
