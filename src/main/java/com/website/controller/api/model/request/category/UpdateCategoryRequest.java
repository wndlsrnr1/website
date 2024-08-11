package com.website.controller.api.model.request.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCategoryRequest {
    @NotNull
    private Long categoryId;
    @NotBlank
    private String name;
    @NotBlank
    private String nameKor;
}
