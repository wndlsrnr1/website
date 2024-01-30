package com.website.web.dto.response.item;

import com.querydsl.core.annotations.QueryProjection;
import com.website.domain.category.Subcategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDetailResponse {
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
    private String savedFileName;
    private String requestName;

    @QueryProjection
    public ItemDetailResponse(Long id, String name, String nameKor, Integer price, Integer quantity, String status, String description, LocalDateTime releasedAt, LocalDateTime updatedAt, LocalDateTime createdAt, Subcategory subcategory, String savedFileName, String requestName) {
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
        this.savedFileName = savedFileName;
        this.requestName = requestName;
    }
}
