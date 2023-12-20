package com.website.web.service.category;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.repository.category.CategoryRepository;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.category.CreateCategoryRequest;
import com.website.web.dto.request.category.CreateSubcategoryRequest;
import com.website.web.dto.request.category.UpdateCategoryRequest;
import com.website.web.dto.request.category.UpdateSubcategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryCRUDService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity createCategory(CreateCategoryRequest request) {
        if (request == null || request.getName() == null || request.getNameKor() == null) {
            return ResponseEntity.badRequest().build();
        }

        Category category = new Category(request.getName(), request.getNameKor());
        Category savedCategory = categoryRepository.saveCategory(category);

        if (savedCategory == null) {
            throw new IllegalArgumentException("category 생성중 오류 생김");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity findCategory(Long id) {
        Category findCategory = categoryRepository.categoryFindById(id);
        if (findCategory == null) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok().body(
                ApiResponseBody.builder().data(findCategory).message("ok").apiError(null)
        );
    }

    public ResponseEntity findCategoryAll() {
        List<Category> findCategories = categoryRepository.categoryFindAll();
        return ResponseEntity.ok(
                ApiResponseBody.builder().data(findCategories).message("ok").build()
        );
    }

    @Transactional
    public ResponseEntity updateCategory(UpdateCategoryRequest request) {
        Category category = categoryRepository.categoryFindById(request.getId());
        if (category == null) {
            throw new IllegalArgumentException("데이터 없음");
        }
        category.setName(request.getName());
        category.setNameKor(request.getNameKor());
        categoryRepository.updateCategory(category);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteCategory(Long id) {
        categoryRepository.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity createSubcategory(CreateSubcategoryRequest request) {
        Long categoryId = request.getCategoryId();
        Category findCategory = categoryRepository.categoryFindById(categoryId);
        if (findCategory == null) {
            throw new IllegalArgumentException("subcategory에 매핑 되는 categoryId가 없음");
        }
        Subcategory subcategory = new Subcategory(findCategory, request.getName(), request.getNameKor());
        Subcategory savedSubcategory = categoryRepository.saveSubcategory(subcategory);
        return ResponseEntity.ok(
                ApiResponseBody.builder().data(savedSubcategory).message("created").build()
        );
    }

    public ResponseEntity findSubcategoriesByCategoryId(Long categoryId) {
        List<Subcategory> subcategories = categoryRepository.subcategoriesFindByCategoryId(categoryId);
        ApiResponseBody<Object> body = ApiResponseBody.builder().data(subcategories).message("ok").build();
        return ResponseEntity.ok().body(body);
    }

    public ResponseEntity findSubcategoryAll() {
        List<Subcategory> subcategories = categoryRepository.subcategoryFindAll();
        ApiResponseBody body = ApiResponseBody.builder().data(subcategories).message("ok").build();
        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity updateSubcategory(UpdateSubcategoryRequest request) {
        Category category = categoryRepository.categoryFindById(request.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException();
        }

        Subcategory subcategory = new Subcategory(category, request.getName(), request.getNameKor());
        subcategory.setId(request.getSubcategoryId());

        categoryRepository.updateSubcategory(subcategory);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteSubcategory(Long id) {
        categoryRepository.deleteSubcategory(id);
        return ResponseEntity.ok().build();
    }

    //do something..

}
