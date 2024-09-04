package com.website.common.controller.category.model.subcategory;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateSubcategoryRequest {
    @NotNull
    private Long subcategoryId;
    @NotNull
    private Long categoryId;
    @NotBlank
    private String name;
    @NotBlank
    private String nameKor;
}
