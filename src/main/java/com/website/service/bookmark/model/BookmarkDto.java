package com.website.service.bookmark.model;

import com.website.repository.bookmark.model.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    private Long id;
    private Long userId;
    private Long itemId;
    private LocalDateTime createdAt;

    public static BookmarkDto of(Bookmark bookmark) {
        return BookmarkDto.builder()
                .id(bookmark.getId())
                .userId(bookmark.getUserId())
                .itemId(bookmark.getItemId())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}
