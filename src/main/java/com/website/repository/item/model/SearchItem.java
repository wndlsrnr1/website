package com.website.repository.item.model;

import com.querydsl.core.annotations.QueryProjection;
import com.website.repository.model.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
public class SearchItem {
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

    @QueryProjection
    public SearchItem(Long itemId, String itemName, String itemNameKor, Integer price, String status, LocalDateTime releasedAt, LocalDateTime createdAt, Long categoryId, String categoryNameKor, Long subcategoryId, String subcategoryNameKor, Long views, Integer salesRate, String brand, String manufacturer, String madeIn, Long attachmentIdForThumbnail) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemNameKor = itemNameKor;
        this.price = price;
        this.status = status;
        this.releasedAt = releasedAt;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.categoryNameKor = categoryNameKor;
        this.subcategoryId = subcategoryId;
        this.subcategoryNameKor = subcategoryNameKor;
        this.views = views;
        this.salesRate = salesRate;
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.madeIn = madeIn;
        this.attachmentIdForThumbnail = attachmentIdForThumbnail;
    }
}
