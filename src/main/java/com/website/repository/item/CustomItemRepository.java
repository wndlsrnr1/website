package com.website.repository.item;

import com.website.controller.api.model.request.item.EditItemRequest;
import com.website.controller.api.model.request.item.EditItemRequestV2;
import com.website.controller.api.model.response.item.CarouselItemResponse;
import com.website.controller.api.model.response.item.ItemBasicResponse;
import com.website.controller.api.model.response.item.ItemDetailResponse;
import com.website.controller.api.model.response.item.ItemResponse;
import com.website.controller.api.model.sqlcond.item.ItemSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CustomItemRepository {
    void updateNameById(String updateName, Long id);

    Page<ItemResponse> getItemResponseByCondByLastItemId(ItemSearchCond itemSearchCond, Pageable pageable, Long lastItemId, Integer lastPage, Integer pageChunk);

    Page<ItemResponse> getItemResponseByCond(ItemSearchCond itemSearchCond, Pageable pageable);

    List<ItemDetailResponse> findItemDetailResponse(Long itemId);

    void deleteFileOnItem(List<Long> fileIdList);

    void updateItemByDto(Long itemId, EditItemRequest editItemRequest);

    void updateItemByDto(Long itemId, EditItemRequestV2 editItemRequest);

    ResponseEntity<List<CarouselItemResponse>> getCarouselItemsInHome();

    Page<ItemResponse> getItemResponseByCondWhenLastPage(ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable, Long lastItemId, Integer lastPageNumber, Integer pageChunk, Boolean isLastPage);

    ItemBasicResponse findItemBasicResponseByItemId(Long itemId);
}