package com.website.common.repository.category.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SubcategoryByCondResponse {
    private Long categoryId;
    private Long subcategoryId;
    private String name;
    private String nameKor;

    @QueryProjection
    public SubcategoryByCondResponse(Long categoryId, Long subcategoryId, String name, String nameKor) {
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.name = name;
        this.nameKor = nameKor;
    }
}
