package com.website.common.repository.item;

import com.website.common.controller.item.model.EditItemRequest;
import com.website.common.controller.item.model.EditItemRequestV2;
import com.website.common.repository.item.model.CarouselItemResponse;
import com.website.common.repository.item.model.ItemBasicResponse;
import com.website.common.repository.item.model.ItemDetailResponse;
import com.website.common.repository.item.model.ItemResponse;
import com.website.common.controller.item.model.ItemSearchCond;
import com.website.common.repository.common.PageResult;
import com.website.common.repository.item.model.ItemWithReview;
import com.website.common.repository.item.model.ItemWithReviewSearchCriteria;
import com.website.common.repository.item.model.SearchItem;
import com.website.common.repository.item.model.SearchItemCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;

import java.util.List;

@Repository
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

    PageResult<SearchItem> search(SearchItemCriteria criteria);

    PageResult<ItemWithReview> searchItemWithReview(ItemWithReviewSearchCriteria dto);
}
