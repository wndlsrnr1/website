package com.website.common.repository.category.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

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
