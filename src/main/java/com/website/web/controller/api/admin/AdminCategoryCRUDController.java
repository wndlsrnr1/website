package com.website.web.controller.api.admin;

import com.website.web.dto.request.category.CreateCategoryRequest;
import com.website.web.dto.request.category.CreateSubcategoryRequest;
import com.website.web.dto.request.category.UpdateCategoryRequest;
import com.website.web.dto.request.category.UpdateSubcategoryRequest;
import com.website.web.dto.sqlcond.category.CategorySearchCond;
import com.website.web.dto.sqlcond.category.SubCategorySearchCond;
import com.website.web.service.category.CategoryCRUDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCategoryCRUDController {

    private final CategoryCRUDService categoryCRUDService;

    //C
    @PutMapping("/category/create")
    public ResponseEntity createCategory(@Validated CreateCategoryRequest request) {
        return categoryCRUDService.createCategory(request);
    }

    //R
    @GetMapping("/category/{category_id}")
    public ResponseEntity getCategory(@PathVariable("category_id") Long id) {
        return categoryCRUDService.findCategory(id);
    }

    @GetMapping("/category")
    public ResponseEntity getCategories() {
        return categoryCRUDService.findCategoryAll();
    }

    //U
    @PostMapping("/category/update")
    public ResponseEntity updateCategory(UpdateCategoryRequest request) {
        return categoryCRUDService.updateCategory(request);
    }

    //D
    @DeleteMapping("/category/delete/{category_id}")
    public ResponseEntity deleteCategory(@PathVariable("category_id") Long id) {
        return categoryCRUDService.deleteCategory(id);
    }

    //[[[[[[[[[[[[[[[[[[SubCategory Start]]]]]]]]]]]]]]]]]]

    //C
    @PutMapping("/subcategory/create")
    public ResponseEntity createSubcategory(CreateSubcategoryRequest request) {
        return categoryCRUDService.createSubcategory(request);
    }

    //R
    @GetMapping("/subcategory")
    public ResponseEntity getSubcategory(@RequestParam("category_id") Long categoryId) {
        return categoryCRUDService.findSubcategoriesByCategoryId(categoryId);
    }

    @GetMapping("/subcategories")
    public ResponseEntity getSubcategories(SubCategorySearchCond subCategorySearchCond, Pageable pageable) {
        log.info("subCategorySearchCond = {}", subCategorySearchCond);
        return categoryCRUDService.pagingSubCategoryByCond(subCategorySearchCond, pageable);
    }

    //U
    @PostMapping("/subcategory/update")
    public ResponseEntity updateSubcategory(UpdateSubcategoryRequest request) {
        return categoryCRUDService.updateSubcategory(request);
    }

    //D
    @DeleteMapping("/subcategory/delete/{subcategory_id}")
    public ResponseEntity deleteSubcategory(@PathVariable("subcategory_id") Long id) {
        return categoryCRUDService.deleteSubcategory(id);
    }

    @GetMapping("/categories")
    public ResponseEntity sendCategoriesResponse(CategorySearchCond categorySearchCond, Pageable pageable) {
        ResponseEntity responseEntity = categoryCRUDService.pagingCategoryByCond(categorySearchCond, pageable);
        return responseEntity;
    }
}
