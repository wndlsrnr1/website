package com.website.web.dto.response.category;

import com.querydsl.core.annotations.QueryProjection;
import com.website.domain.category.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class CategoryByCondResponse {
    private Long id;
    private String name;
    private String nameKor;

    @QueryProjection
    public CategoryByCondResponse(Long id, String name, String nameKor) {
        this.id = id;
        this.name = name;
        this.nameKor = nameKor;
    }

}
