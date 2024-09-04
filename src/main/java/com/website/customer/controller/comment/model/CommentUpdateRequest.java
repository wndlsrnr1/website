package com.website.customer.controller.comment.model;

import com.website.customer.service.comment.CommentUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
