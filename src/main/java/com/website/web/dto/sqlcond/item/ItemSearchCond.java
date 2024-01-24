package com.website.web.dto.sqlcond.item;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemSearchCond {

    private Long itemId;
    private String name;
    private String nameKor;
    private Integer price;
    private Integer quantity;
    private String status;
    private String description;
    private LocalDateTime releaseDate;
    private Long subcategoryId;

}
