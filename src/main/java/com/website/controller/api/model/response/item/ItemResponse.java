package com.website.controller.api.model.response.item;

import com.querydsl.core.annotations.QueryProjection;
import com.website.repository.model.category.Subcategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemResponse {
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

    @QueryProjection
    public ItemResponse(Long id, String name, String nameKor, Integer price, Integer quantity, String status, String description, LocalDateTime releasedAt, LocalDateTime updatedAt, LocalDateTime createdAt, Subcategory subcategory) {
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
    }

    @QueryProjection
    public ItemResponse(Long id, String name, String nameKor, Subcategory subcategory) {
        this.id = id;
        this.name = name;
        this.nameKor = nameKor;
        this.subcategory = subcategory;
    }
}
