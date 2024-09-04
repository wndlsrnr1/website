package com.website.common.repository.category;

import com.website.common.repository.model.category.Category;
import com.website.common.repository.category.model.CategoryByCondResponse;
import com.website.common.controller.category.model.CategorySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryCustomRepository {

    Page<CategoryByCondResponse> searchPageByCond(CategorySearchCond categorySearchCond, Pageable pageable);

    Category findBySubCategoryId(Long subcategoryIdLong);
}
