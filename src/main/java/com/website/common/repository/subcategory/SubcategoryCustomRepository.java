package com.website.common.repository.subcategory;

import com.website.common.repository.model.category.Subcategory;
import com.website.common.repository.category.model.SubcategoryByCondResponse;
import com.website.common.controller.category.model.SubCategorySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubcategoryCustomRepository {

    List<Subcategory> findByCategoryId(Long categoryId);

    List<Subcategory> findAll(int limit);

    Page<SubcategoryByCondResponse> searchPageByCond(SubCategorySearchCond subCategorySearchCond, Pageable pageable);

}
