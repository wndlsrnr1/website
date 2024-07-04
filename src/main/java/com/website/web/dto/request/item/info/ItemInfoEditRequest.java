package com.website.web.dto.request.item.info;

import lombok.Data;
import lombok.Setter;

@Data
public class ItemInfoEditRequest {
    private Long itemInfoId;
    private Long itemId;
    private Long views;
    private Integer salesRate;
    private String brand;
    private String manufacturer;
    private String madeIn;

    public ItemInfoEditRequest(Long itemInfoId, Long itemId, Integer salesRate, String brand, String manufacturer, String madeIn) {
        this.itemInfoId = itemInfoId;
        this.itemId = itemId;
        this.salesRate = salesRate;
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.madeIn = madeIn;
    }
}
