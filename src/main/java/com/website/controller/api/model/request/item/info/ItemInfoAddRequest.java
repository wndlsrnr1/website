package com.website.controller.api.model.request.item.info;

import lombok.Data;

@Data
public class ItemInfoAddRequest {
    private Long itemId;
    private Long views;
    private Integer salesRate;
    private String brand;
    private String manufacturer;
    private String madeIn;

    public ItemInfoAddRequest(Long itemId, Integer salesRate, String brand, String manufacturer, String madeIn) {
        this.itemId = itemId;
        this.salesRate = salesRate;
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.madeIn = madeIn;
        this.views = 0L;
    }

}
