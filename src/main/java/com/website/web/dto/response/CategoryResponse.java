package com.website.web.dto.response;

import com.website.domain.category.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String nameKor;
    private List<Subcategory> subcategories;
}
