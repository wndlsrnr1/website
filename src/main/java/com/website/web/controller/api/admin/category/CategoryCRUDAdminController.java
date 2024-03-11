package com.website.web.controller.api.admin.category;

import com.website.web.dto.request.category.CreateCategoryRequest;
import com.website.web.dto.request.category.subcategory.CreateSubcategoryRequest;
import com.website.web.dto.request.category.UpdateCategoryRequest;
import com.website.web.dto.request.category.subcategory.UpdateSubcategoryRequest;
import com.website.web.dto.sqlcond.category.CategorySearchCond;
import com.website.web.dto.sqlcond.category.SubCategorySearchCond;
import com.website.web.service.category.CategoryCRUDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CategoryCRUDAdminController {

    private final CategoryCRUDService categoryCRUDService;

    //category
    @PutMapping("/category/create")
    public ResponseEntity createCategory(@Validated CreateCategoryRequest request) {
        return categoryCRUDService.createCategory(request);
    }

    @GetMapping(value = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCategory(@PathVariable("category_id") Long id) {
        log.info("id = {}", id);
        return categoryCRUDService.findCategory(id);
    }

    //@GetMapping("/category")
    public ResponseEntity getCategories() {
        return categoryCRUDService.findCategoryAll();
    }

    @GetMapping("/category")
    public ResponseEntity getCategory(HttpServletRequest request, HttpServletResponse response) {
        return categoryCRUDService.sendCategory(request, response);
    }

    @DeleteMapping("/category/delete/{category_id}")
    public ResponseEntity deleteCategory(@PathVariable("category_id") Long id) {
        return categoryCRUDService.deleteCategory(id);
    }

    @GetMapping("/categories")
    public ResponseEntity sendCategoriesResponse(CategorySearchCond categorySearchCond, Pageable pageable) {
        ResponseEntity responseEntity = categoryCRUDService.pagingCategoryByCond(categorySearchCond, pageable);
        return responseEntity;
    }

    @PostMapping("/categories/update")
    public ResponseEntity updateCategoryByRequest(@ModelAttribute UpdateCategoryRequest updateCategoryRequest) {
        return categoryCRUDService.updateCategory(updateCategoryRequest);
    }

    //Subcategory

    @PutMapping("/subcategory/create")
    public ResponseEntity createSubcategory(@Validated CreateSubcategoryRequest request, BindingResult bindingResult) {
        return categoryCRUDService.createSubcategoryByDTO(request, bindingResult);
    }

    @GetMapping("/subcategory")
    public ResponseEntity getSubcategory(@RequestParam("category_id") Long categoryId) {
        return categoryCRUDService.findSubcategoriesByCategoryId(categoryId);
    }

    @GetMapping("/subcategories")
    public ResponseEntity getSubcategories(SubCategorySearchCond subCategorySearchCond, Pageable pageable) {
        log.info("subCategorySearchCond = {}", subCategorySearchCond);
        return categoryCRUDService.pagingSubCategoryByCond(subCategorySearchCond, pageable);
    }

    @PostMapping("/subcategory/update")
    public ResponseEntity updateSubcategory(@Validated UpdateSubcategoryRequest request, BindingResult bindingResult) {
        return categoryCRUDService.updateSubcategoryByDto(request, bindingResult);
    }

    @DeleteMapping("/subcategory/delete/{subcategoryId}")
    public ResponseEntity deleteSubcategory(@PathVariable("subcategoryId") Long subcategoryId) {
        return categoryCRUDService.deleteSubcategoryByPathVariable(subcategoryId);
    }



}
