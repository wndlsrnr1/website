package com.website.web.service.category;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.repository.category.CategoryRepository;
//import com.website.repository.category.CategoryRepositoryByJpaAndQueryDsl;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.category.*;
import com.website.web.dto.response.category.CategoryByCondResponse;
import com.website.web.dto.response.category.SubcategoryByCondResponse;
import com.website.web.dto.sqlcond.category.CategorySearchCond;
import com.website.web.dto.sqlcond.category.SubCategorySearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryCRUDService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    @Transactional
    public ResponseEntity createCategory(CreateCategoryRequest request) {
        if (request == null || request.getName() == null || request.getNameKor() == null) {
            return ResponseEntity.badRequest().build();
        }

        Category category = new Category(request.getName(), request.getNameKor());
        Category savedCategory = categoryRepository.save(category);

        if (savedCategory == null) {
            throw new IllegalArgumentException("category 생성중 오류 생김");
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity findCategory(Long id) {
        Category findCategory = categoryRepository.findById(id).orElseGet(null);
        if (findCategory == null) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok().body(
                ApiResponseBody.builder().data(findCategory).message("ok").apiError(null)
        );
    }

    public ResponseEntity findCategoryAll() {
        List<Category> findCategories = categoryRepository.findAll(Sort.by("name"));
        ApiResponseBody<Object> body = ApiResponseBody.builder().data(findCategories).message("ok").build();
        log.info("findCategories = {}", body);
        return ResponseEntity.ok(
                body
        );
    }

    @Transactional
    public ResponseEntity updateCategory(UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(request.getId()).orElseGet(null);
        if (category == null) {
            throw new IllegalArgumentException("데이터 없음");
        }
        category.setName(request.getName());
        category.setNameKor(request.getNameKor());
        categoryRepository.save(category);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity createSubcategory(CreateSubcategoryRequest request) {
        Long categoryId = request.getCategoryId();
        Category findCategory = categoryRepository.findById(categoryId).orElseGet(null);
        if (findCategory == null) {
            throw new IllegalArgumentException("subcategory에 매핑 되는 categoryId가 없음");
        }
        Subcategory subcategory = new Subcategory(findCategory, request.getName(), request.getNameKor());
        subcategoryRepository.save(subcategory);
        return ResponseEntity.ok(
                ApiResponseBody.builder().data(subcategory).message("created").build()
        );
    }

    public ResponseEntity findSubcategoriesByCategoryId(Long categoryId) {
        List<Subcategory> subcategories = subcategoryRepository.findByCategoryId(categoryId);
        ApiResponseBody<Object> body = ApiResponseBody.builder().data(subcategories).message("ok").build();
        return ResponseEntity.ok().body(body);
    }

    public ResponseEntity findSubcategoryAll() {
        List<Subcategory> subcategories = subcategoryRepository.findAll(100);
        ApiResponseBody body = ApiResponseBody.builder().data(subcategories).message("ok").build();
        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity updateSubcategory(UpdateSubcategoryRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseGet(null);
        if (category == null) {
            throw new IllegalArgumentException();
        }

        Subcategory subcategory = new Subcategory(category, request.getName(), request.getNameKor());
        subcategory.setId(request.getSubcategoryId());

        subcategoryRepository.save(subcategory);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteSubcategory(Long id) {
        subcategoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity pagingCategoryByCond(CategorySearchCond categorySearchCond, Pageable pageable) {
        Page<CategoryByCondResponse> categoryByCondResponses = categoryRepository.searchPageByCond(categorySearchCond, pageable);
        ApiResponseBody<Page<CategoryByCondResponse>> body = ApiResponseBody.<Page<CategoryByCondResponse>>builder()
                .data(categoryByCondResponses)
                .message("ok")
                .apiError(null)
                .build();
        return ResponseEntity.ok().body(body);
    }

    public ResponseEntity pagingSubCategoryByCond(SubCategorySearchCond subCategorySearchCond, Pageable pageable) {
        Page<SubcategoryByCondResponse> subcategoryResponses = subcategoryRepository.searchPageByCond(subCategorySearchCond, pageable);
        ApiResponseBody body = ApiResponseBody.builder()
                .data(subcategoryResponses)
                .message("ok")
                .apiError(null)
                .build();
        log.info("subcategoryResponses = {}", subcategoryResponses);
        return ResponseEntity.ok().body(body);
    }

    //do something..

}
