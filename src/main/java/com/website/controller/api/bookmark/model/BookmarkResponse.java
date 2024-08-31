package com.website.controller.api.bookmark.model;

import com.website.service.bookmark.model.BookmarkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    private Long id;
    private Long itemId;
    private LocalDateTime createdAt;

    public static BookmarkResponse of(BookmarkDto dto) {
        return BookmarkResponse.builder()
                .id(dto.getId())
                .itemId(dto.getItemId())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
