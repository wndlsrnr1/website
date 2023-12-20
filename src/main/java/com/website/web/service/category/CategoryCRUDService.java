package com.website.web.service.category;

import com.website.domain.category.Category;
import com.website.repository.category.CategoryRepository;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.category.CreateCategoryRequest;
import com.website.web.dto.request.category.UpdateCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryCRUDService {

    private final CategoryRepository categoryRepository;

    public ResponseEntity createCategory(CreateCategoryRequest request) {
        if (request == null || request.getName() == null || request.getNameKor() == null) {
            return ResponseEntity.badRequest().build();
        }

        Category category = new Category(request.getName(), request.getNameKor());
        Category savedCategory = categoryRepository.saveCategory(category);

        if (savedCategory != null) {
            return ResponseEntity.badRequest().build();
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

    public ResponseEntity updateCategory(UpdateCategoryRequest request) {
        Category category = categoryRepository.categoryFindById(request.getId());
        category.setName(request.getName());
        category.setNameKor(request.getNameKor());
        categoryRepository.updateCategory(category);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteCategory(Long id) {
        categoryRepository.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    //do something..

}
