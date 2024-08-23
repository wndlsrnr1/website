package com.website.service.comment.model;

import com.website.repository.comment.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long itemId;
    private String username;
    private Integer star;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentDto of(Comment savedComment) {
        return CommentDto.builder()
                .id(savedComment.getId())
                .itemId(savedComment.getItem().getId())
                .username(savedComment.getUser().getName())
                .star(savedComment.getStar())
                .content(savedComment.getContent())
                .createdAt(savedComment.getCreatedAt())
                .updatedAt(savedComment.getUpdatedAt())
                .build();
    }

}
