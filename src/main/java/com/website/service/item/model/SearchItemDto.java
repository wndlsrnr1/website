package com.website.service.item.model;

import com.website.repository.item.model.SearchItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchItemDto {

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
    private Long attachmentIdForThumbnail; //17


    public static SearchItemDto of(SearchItem searchItem) {
        return SearchItemDto.builder()
                .itemId(searchItem.getItemId())
                .itemName(searchItem.getItemName())
                .itemNameKor(searchItem.getItemNameKor())
                .price(searchItem.getPrice())
                .status(searchItem.getStatus())
                .categoryId(searchItem.getCategoryId())
                .categoryNameKor(searchItem.getCategoryNameKor())
                .subcategoryId(searchItem.getSubcategoryId())
                .subcategoryNameKor(searchItem.getSubcategoryNameKor())
                .views(searchItem.getViews())
                .salesRate(searchItem.getSalesRate())
                .brand(searchItem.getBrand())
                .manufacturer(searchItem.getManufacturer())
                .madeIn(searchItem.getMadeIn())
                .attachmentIdForThumbnail(searchItem.getAttachmentIdForThumbnail())
                .releasedAt(searchItem.getReleasedAt())
                .createdAt(searchItem.getCreatedAt())
                .build();
    }
}
