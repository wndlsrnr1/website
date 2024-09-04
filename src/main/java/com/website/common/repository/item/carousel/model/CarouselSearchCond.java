package com.website.common.repository.item.carousel.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class CarouselSearchCond {
    @Min(value = 0L)
    private Integer priceMin;

    @Max(value = Integer.MAX_VALUE)
    private Integer priceMax;

    @Min(value = 0L)
    private Integer quantityMin;

    @Max(value = Integer.MAX_VALUE)
    private Integer quantityMax;

    @Min(-1L)
    private Long categoryId;

    private String searchName;
}
