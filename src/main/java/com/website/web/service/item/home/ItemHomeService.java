package com.website.web.service.item.home;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.repository.item.ItemRepository;
import com.website.repository.item.customer.ItemCustomerRepository;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.response.category.home.SubcategoryResponse;
import com.website.web.dto.response.item.home.ItemLatestResponse;
import com.website.web.dto.response.item.home.ItemPopularResponse;
import com.website.web.dto.response.item.home.ItemSpecialResponse;
import com.website.web.dto.response.item.home.ItemsForCustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemHomeService {

    private final ItemRepository itemRepository;
    private final ItemCustomerRepository itemCustomerRepository;
    private final SubcategoryRepository subcategoryRepository;

    public ResponseEntity getItemsResponseLatest() {
        //List<ItemLatestResponse> itemList = itemRepository.getLatestProducts();
        List<ItemLatestResponse> itemList = itemCustomerRepository.getLatestProducts();
        return ResponseEntity.ok(itemList);
    }

    public ResponseEntity getItemsResponseSpecialSale() {
        List<ItemSpecialResponse> itemList = itemCustomerRepository.getSpecialSaleProducts();
        return ResponseEntity.ok(itemList);
    }

    public ResponseEntity getItemsResponsePopular() {
        List<ItemPopularResponse> itemList = itemCustomerRepository.getPopularProducts();
        return ResponseEntity.ok(itemList);
    }

    public ResponseEntity sendItemsResponseByCondByLastItemId(
            String sortedBy, Pageable pageable, Long lastItemId, Integer lastPageNumber, Integer pageChunk, Boolean isLastPage,
            Long subcategoryId, Long totalItems) {

        //log.info("sortedBy = {}", sortedBy);
        //log.info("Pageable = {}", pageable);
        //log.info("lastItemId = {}", lastItemId);
        //log.info("lastPageNumber = {}", lastPageNumber);
        //log.info("pageChunk = {}", pageChunk);
        //log.info("isLastPage = {}", isLastPage);
        //log.info("subcategoryId = {}", subcategoryId);

        //정상 흐름
        Page<ItemsForCustomerResponse> itemsForCustomerResponsePage = itemCustomerRepository.getItemsForCustomerResponseByCondByLastItemId(subcategoryId, sortedBy, totalItems, lastItemId, pageable);

        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(itemsForCustomerResponsePage)
                .apiError(null)
                .message("ok")
                .build();

        return ResponseEntity.ok(body);
    }


    //public ResponseEntity<? extends ApiResponseBody<?>> getSubcategoryInfoResponse(Long subcategoryId) {
    public ResponseEntity getSubcategoryInfoResponse(Long subcategoryId) {
        if (subcategoryId == null) {
            return ResponseEntity.badRequest().build();
        }

        if (subcategoryId < 0) {
            return ResponseEntity.badRequest().build();
        }

        Subcategory subcategory = subcategoryRepository.findById(subcategoryId).orElse(null);

        if (subcategory == null) {
            return ResponseEntity.badRequest().build();
        }

        Category category = subcategory.getCategory();
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(
                ApiResponseBody.builder().data(
                        new SubcategoryResponse(
                                category.getId(),
                                category.getName(),
                                category.getNameKor(),
                                subcategory.getId(),
                                subcategory.getName(),
                                subcategory.getNameKor()
                        )
                ).build()
        );
    }
}
