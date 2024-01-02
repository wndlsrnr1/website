package com.website.web.dto.sqlcond.category;

import lombok.Data;

@Data
public class SubCategorySearchCond {
    private Long categoryId;
    private String name;
    private String nameKor;
    private Integer page;
    private Integer totalPage;
}
