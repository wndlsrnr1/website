package com.website.web.dto.request.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCategoryRequest {
    @NotBlank
    String name;
    @NotBlank
    String nameKor;
}
