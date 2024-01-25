package com.website.web.dto.sqlcond.item;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class ItemSearchCond {

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
