package com.website.repository.subcategory;

import com.website.domain.category.Subcategory;

import java.util.List;

public interface SubcategoryCustomRepository {

    List<Subcategory> findByCategoryId(Long categoryId);

    List<Subcategory> findAll(int limit);
}
