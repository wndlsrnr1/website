package com.website.repository.category;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;

import java.util.List;

public interface CategoryRepository {
    //Create
    Category saveCategory(Category category);

    Subcategory saveSubcategory(Subcategory subcategory);

    //Read
    Category categoryFindById(Long id);

    Subcategory subcategoryFindById(Long id);

    //Update
    Category updateCategory(Category category);

    Subcategory updateSubcategory(Subcategory subcategory);

    //Delete
    void deleteCategory(Long id);

    void deleteSubcategory(Long id);

    List<Category> categoryFindAll();

    List<Subcategory> subcategoriesFindByCategoryId(Long categoryId);

    List<Subcategory> subcategoryFindAll();

    List<Subcategory> findCategoriesAndSubCategories();
}
