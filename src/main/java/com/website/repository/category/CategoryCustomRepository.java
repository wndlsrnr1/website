package com.website.repository.category;

import com.website.repository.model.category.Category;
import com.website.controller.api.model.response.category.CategoryByCondResponse;
import com.website.controller.api.model.sqlcond.category.CategorySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryCustomRepository {

    Page<CategoryByCondResponse> searchPageByCond(CategorySearchCond categorySearchCond, Pageable pageable);

    Category findBySubCategoryId(Long subcategoryIdLong);
}
