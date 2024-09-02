package com.website.controller.api.comment.model;

import com.website.service.comment.model.CommentUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {

    @NotBlank
    private String content;

    public CommentUpdateDto toDto(Long userId) {
        return CommentUpdateDto.builder()
                .userId(userId)
                .content(content)
                .build();
    }
}
