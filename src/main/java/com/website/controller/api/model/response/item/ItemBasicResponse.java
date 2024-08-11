package com.website.controller.api.model.response.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemBasicResponse {

    private Long itemId;
    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private LocalDateTime releasedAt;
    private Long views;
    private Integer salesRate;
    private String brand;
    private String manufacturer;
    private String madeIn;

    @QueryProjection
    public ItemBasicResponse(Long itemId, String name, String nameKor, Integer price, Integer quantity, String status, String description, LocalDateTime releasedAt, Long views, Integer salesRate, String brand, String manufacturer, String madeIn) {
        this.itemId = itemId;
        this.name = name;
        this.nameKor = nameKor;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
        this.releasedAt = releasedAt;
        this.views = views;
        this.salesRate = salesRate;
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.madeIn = madeIn;
    }
}
