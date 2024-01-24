package com.website.repository.subcategory;

import com.website.domain.category.Subcategory;
import com.website.web.dto.request.category.subcategory.CreateSubcategoryRequest;
import com.website.web.dto.response.category.SubcategoryByCondResponse;
import com.website.web.dto.sqlcond.category.SubCategorySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubcategoryCustomRepository {

    List<Subcategory> findByCategoryId(Long categoryId);

    List<Subcategory> findAll(int limit);

    Page<SubcategoryByCondResponse> searchPageByCond(SubCategorySearchCond subCategorySearchCond, Pageable pageable);

}
