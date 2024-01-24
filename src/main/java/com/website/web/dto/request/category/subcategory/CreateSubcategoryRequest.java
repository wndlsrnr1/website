package com.website.web.dto.request.category.subcategory;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateSubcategoryRequest {
    @NotNull
    @Min(1L)
    private Long categoryId;
    @NotBlank
    private String name;
    @NotBlank
    private String nameKor;
}
