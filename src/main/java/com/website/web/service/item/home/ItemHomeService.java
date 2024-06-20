package com.website.web.service.item.home;

import com.website.repository.item.ItemRepository;
import com.website.repository.item.customer.ItemCustomerRepository;
import com.website.web.dto.common.ApiResponseBody;
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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemHomeService {

    private final ItemRepository itemRepository;
    private final ItemCustomerRepository itemCustomerRepository;

    public ResponseEntity getItemsResponseLatest() {
        List<ItemLatestResponse> itemList = itemRepository.getLatestProducts();
        //List<ItemLatestResponse> itemList = itemCustomerRepository.getLatestProducts();
        return ResponseEntity.ok(itemList);
    }

    public ResponseEntity getItemsResponseSpecialSale() {
        //List<ItemSpecialResponse> itemList = itemRepository.getSpecialSaleProducts();
        List<ItemSpecialResponse> itemList = itemRepository.getSpecialSaleProducts();
        return ResponseEntity.ok(itemList);
    }

    public ResponseEntity getItemsResponsePopular() {
        //List<ItemPopularResponse> itemList = itemRepository.getPopularProducts();
        List<ItemPopularResponse> itemList = itemRepository.getPopularProducts();
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
}
