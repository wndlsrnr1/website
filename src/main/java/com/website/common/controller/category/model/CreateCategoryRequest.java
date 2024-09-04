package com.website.common.controller.category.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCategoryRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String nameKor;
}
