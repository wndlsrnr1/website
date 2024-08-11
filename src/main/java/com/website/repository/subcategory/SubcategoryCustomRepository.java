package com.website.repository.subcategory;

import com.website.repository.model.category.Subcategory;
import com.website.controller.api.model.response.category.SubcategoryByCondResponse;
import com.website.controller.api.model.sqlcond.category.SubCategorySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubcategoryCustomRepository {

    List<Subcategory> findByCategoryId(Long categoryId);

    List<Subcategory> findAll(int limit);

    Page<SubcategoryByCondResponse> searchPageByCond(SubCategorySearchCond subCategorySearchCond, Pageable pageable);

}
