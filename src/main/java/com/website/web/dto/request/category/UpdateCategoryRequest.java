package com.website.web.dto.request.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCategoryRequest {
    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String nameKor;
}
