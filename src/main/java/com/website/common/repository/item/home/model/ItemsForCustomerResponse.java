package com.website.common.repository.item.home.model;

import com.querydsl.core.annotations.QueryProjection;
import com.website.common.repository.model.category.Subcategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemsForCustomerResponse {
    private Long id;
    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private LocalDateTime releasedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private Subcategory subcategory;
    private Integer saleRate;
    private Long views;
    private Long fileIdForThumbnail;

    @QueryProjection
    public ItemsForCustomerResponse(Long id, String name, String nameKor, Integer price, Integer quantity, String status, String description, LocalDateTime releasedAt, LocalDateTime updatedAt, LocalDateTime createdAt, Subcategory subcategory, Integer saleRate, Long views, Long fileIdForThumbnail) {
        this.id = id;
        this.name = name;
        this.nameKor = nameKor;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
        this.releasedAt = releasedAt;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.subcategory = subcategory;
        this.saleRate = saleRate;
        this.views = views;
        this.fileIdForThumbnail = fileIdForThumbnail;
    }
}
