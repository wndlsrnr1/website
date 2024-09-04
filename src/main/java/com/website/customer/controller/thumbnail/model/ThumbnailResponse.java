package com.website.customer.controller.thumbnail.model;

import com.website.common.controller.model.ApiResponse;
import com.website.customer.service.thumbnail.model.ThumbnailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ThumbnailResponse {
    private Long fileId;
    private Long itemId;

    public static ThumbnailResponse of(ThumbnailResponseDto dto) {
        return ThumbnailResponse.builder()
                .fileId(dto.getFileId())
                .itemId(dto.getItemId())
                .build();
    }
}
