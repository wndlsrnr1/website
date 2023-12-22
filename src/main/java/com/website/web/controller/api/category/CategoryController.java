package com.website.web.controller.api.category;

import com.website.web.service.category.CategoryCRUDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryCRUDService categoryCRUDService;

    @GetMapping("/{category_id}")
    public ResponseEntity getCategory(@PathVariable("category_id") Long id) {
        return categoryCRUDService.findCategory(id);
    }

    @GetMapping
    public ResponseEntity getCategoriesAndSubCategories() {
        log.info("sadfasdf");
        return categoryCRUDService.findCategoryAll();
    }
}
