package com.website.customer.service.thumbnail.model;

import com.website.common.repository.model.item.ItemThumbnail;
import com.website.customer.controller.thumbnail.model.ThumbnailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThumbnailResponseDto {
    private Long fileId;
    private Long itemId;

    public static ThumbnailResponseDto of(ItemThumbnail itemThumbnail) {
        return ThumbnailResponseDto.builder()
                .fileId(itemThumbnail.getAttachment().getId())
                .itemId(itemThumbnail.getItem().getId())
                .build();
    }
}
