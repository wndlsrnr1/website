package com.website.controller.api.category;

import com.website.controller.api.model.sqlcond.category.CategorySearchCond;
import com.website.service.category.CategoryCRUDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryCRUDService categoryCRUDService;

    @GetMapping("/{category_id}")
    public ResponseEntity getCategory(@PathVariable("category_id") Long id) {
        return categoryCRUDService.findCategory(id);
    }

    @GetMapping
    public ResponseEntity sendCategoriesResponse(@ModelAttribute CategorySearchCond categorySearchCond) {
        log.info("[[[sendCategoriesResponse]]]");
        return categoryCRUDService.findCategoryAll();
    }
}
