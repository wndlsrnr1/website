package com.website.web.dto.response.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ItemInfoResponse {
    private Long id;
    private Long itemId;
    private Long views;
    private String brand;
    private Integer saleRate;
    private String madeIn;
    private String manufacturer;

    @QueryProjection
    public ItemInfoResponse(Long id, Long itemId, Long views, String brand, Integer saleRate, String madeIn, String manufacturer) {
        this.id = id;
        this.itemId = itemId;
        this.views = views;
        this.brand = brand;
        this.saleRate = saleRate;
        this.madeIn = madeIn;
        this.manufacturer = manufacturer;
    }

}
