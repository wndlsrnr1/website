package com.website.customer.controller.item.model;

import com.website.common.service.item.model.SearchItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchItemResponse {

    private Long itemId;
    private String itemName;
    private String itemNameKor;
    private Integer price;
    private String status;
    private LocalDateTime releasedAt;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String categoryNameKor;
    private Long subcategoryId;
    private String subcategoryNameKor;
    private Long views;
    private Integer salesRate;
    private String brand;
    private String manufacturer;
    private String madeIn;
    private Long attachmentIdForThumbnail;

    public static SearchItemResponse of(SearchItemDto dto) {
        return SearchItemResponse.builder()
                .itemId(dto.getItemId())
                .itemName(dto.getItemName())
                .itemNameKor(dto.getItemNameKor())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .categoryId(dto.getCategoryId())
                .categoryNameKor(dto.getCategoryNameKor())
                .subcategoryId(dto.getSubcategoryId())
                .subcategoryNameKor(dto.getSubcategoryNameKor())
                .views(dto.getViews())
                .salesRate(dto.getSalesRate())
                .brand(dto.getBrand())
                .manufacturer(dto.getManufacturer())
                .madeIn(dto.getMadeIn())
                .attachmentIdForThumbnail(dto.getAttachmentIdForThumbnail())
                .releasedAt(dto.getReleasedAt())
                .createdAt(dto.getCreatedAt())
                .build()
                ;
    }
}
