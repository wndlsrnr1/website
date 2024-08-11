package com.website.controller.api.model.response.category.home;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SubcategoryResponse {
    private Long categoryId;
    private String categoryName;
    private String categoryNameKor;
    private Long subcategoryId;
    private String subcategoryName;
    private String subcategoryNameKor;

    @QueryProjection
    public SubcategoryResponse(Long categoryId, String categoryName, String categoryNameKor, Long subcategoryId, String subcategoryName, String subcategoryNameKor) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryNameKor = categoryNameKor;
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
        this.subcategoryNameKor = subcategoryNameKor;
    }

    public SubcategoryResponse() {
    }
}
