package com.website.repository.category;

import com.website.domain.category.Category;
import com.website.web.dto.response.category.CategoryByCondResponse;
import com.website.web.dto.sqlcond.category.CategorySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryCustomRepository {

    Page<CategoryByCondResponse> searchPageByCond(CategorySearchCond categorySearchCond, Pageable pageable);

    Category findBySubCategoryId(Long subcategoryIdLong);
}
