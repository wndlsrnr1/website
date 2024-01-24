package com.website.web.service.category;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.repository.category.CategoryRepository;
//import com.website.repository.category.CategoryRepositoryByJpaAndQueryDsl;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.category.*;
import com.website.web.dto.request.category.subcategory.CreateSubcategoryRequest;
import com.website.web.dto.request.category.subcategory.UpdateSubcategoryRequest;
import com.website.web.dto.response.category.CategoryByCondResponse;
import com.website.web.dto.response.category.SubcategoryByCondResponse;
import com.website.web.dto.sqlcond.category.CategorySearchCond;
import com.website.web.dto.sqlcond.category.SubCategorySearchCond;
import com.website.web.service.common.BindingResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryCRUDService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BindingResultUtils bindingResultUtils;
    private final MessageSource messageSource;

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
        Category findCategory = categoryRepository.findById(id).orElse(null);
        if (findCategory == null) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok().body(
                ApiResponseBody.builder().data(findCategory).message("ok").apiError(null).build()
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

        Category category = categoryRepository.findById(request.getCategoryId()).orElseGet(null);
        if (category == null) {
            throw new IllegalArgumentException("데이터 없음");
        }

        category.setName(request.getName());
        category.setNameKor(request.getNameKor());
        categoryRepository.save(category);
        ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(null).data(category).message("updated").build();
        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity createSubcategory(CreateSubcategoryRequest request) {
        if (request.getCategoryId() == -1) {
            return ResponseEntity.badRequest().build();
        }

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

    @Transactional
    public ResponseEntity createSubcategoryByDTO(CreateSubcategoryRequest request, BindingResult bindingResult) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        //바인딩 결과에 문제가 있을 때
        if (bindingResult.hasErrors()) {
            ApiResponseBody<Object> body = getApiReponseHasBindingError(bindingResult, null, "has binding Error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        //무결성에 문제가 있을때
        Long categoryId = request.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            bindingResultUtils.addFieldMessagesTo(bindingResult, "categoryId", "Nodata.categoryId");
            ApiResponseBody<Object> body = getApiReponseHasBindingError(bindingResult, null, "no categoryId");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        //그외 서비스상 조건을 만족하지 못 했을 때

        //성공 흐름
        subcategoryRepository.save(new Subcategory(category, request.getName(), request.getNameKor()));

        return ResponseEntity.ok().build();
    }

    private ApiResponseBody<Object> getApiReponseHasBindingError(BindingResult bindingResult, Object data, String message) {
        return ApiResponseBody.builder()
                .apiError(new ApiError(bindingResult))
                .data(data)
                .message(message)
                .build();
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

    @Transactional
    public ResponseEntity updateSubcategoryByDto(UpdateSubcategoryRequest request, BindingResult bindingResult) {
        //바인딩 오류가 있을때
        if (bindingResult.hasErrors()) {
            ApiResponseBody<Object> body = getApiReponseHasBindingError(bindingResult, null, "has binding Error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        //DB inconsistency
        Long categoryId = request.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            bindingResultUtils.addFieldMessagesTo(bindingResult, "categoryId", "Nodata.categoryId");
            ApiResponseBody<Object> body = getApiReponseHasBindingError(bindingResult, null, "has binding Error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        Long subcategoryId = request.getSubcategoryId();
        Subcategory subcategory = subcategoryRepository.findById(subcategoryId).orElse(null);
        if (subcategory == null) {
            bindingResultUtils.addFieldMessagesTo(bindingResult, "subcategoryId", "Nodata.subcategoryId");
            ApiResponseBody<Object> body = getApiReponseHasBindingError(bindingResult, null, "has binding Error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        //그외 서비스상 문제

        //정상 흐름
        subcategory.setCategory(category);
        subcategory.setName(request.getName());
        subcategory.setNameKor(request.getNameKor());
        subcategoryRepository.save(subcategory);
        ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(null).data(null).message("ok").build();
        return ResponseEntity.ok().body(body);
    }

    @Transactional
    public ResponseEntity deleteSubcategoryByPathVariable(Long subcategoryId) {
        //parameter error
        if (subcategoryId == null) {
            String message = messageSource.getMessage("NotNull.subcategoryId", null, null);
            String field = "subcategoryId";
            ApiError error = new ApiError(field, message);
            ApiResponseBody<Object> body = ApiResponseBody.builder().data(null).message("has binding error").apiError(error).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        //inconsis
        Subcategory subcategory = subcategoryRepository.findById(subcategoryId).orElse(null);
        if (subcategory == null) {
            String message = messageSource.getMessage("Nodata.subcategoryId", null, null);
            String field = "subcategoryId";
            ApiError error = new ApiError(field, message);
            ApiResponseBody<Object> body = ApiResponseBody.builder().data(null).message("해당하는 서브카테고리가 없음").apiError(error).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        //성공 흐름
        subcategoryRepository.delete(subcategory);
        ApiResponseBody body = ApiResponseBody.builder().apiError(null).data(null).message("deleted").build();
        return ResponseEntity.ok().body(body);
    }

    //do something..

}
