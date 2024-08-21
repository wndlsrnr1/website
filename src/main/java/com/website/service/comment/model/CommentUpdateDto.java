package com.website.service.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateDto {
    private Long commentId;
    private Long userId;
    private Integer star;
    private String content;
}
