package com.website.repository.item;

import com.website.web.dto.request.item.EditItemRequest;
import com.website.web.dto.request.item.EditItemRequestV2;
import com.website.web.dto.request.item.home.ItemHomeSearchCond;
import com.website.web.dto.response.item.CarouselItemResponse;
import com.website.web.dto.response.item.ItemDetailResponse;
import com.website.web.dto.response.item.ItemResponse;
import com.website.web.dto.response.item.home.ItemLatestResponse;
import com.website.web.dto.response.item.home.ItemPopularResponse;
import com.website.web.dto.response.item.home.ItemSpecialResponse;
import com.website.web.dto.response.item.home.ItemsForCustomerResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ItemCustomRepository {
    void updateNameById(String updateName, Long id);

    Page<ItemResponse> getItemResponseByCondByLastItemId(ItemSearchCond itemSearchCond, Pageable pageable, Long lastItemId, Integer lastPage, Integer pageChunk);

    Page<ItemResponse> getItemResponseByCond(ItemSearchCond itemSearchCond, Pageable pageable);

    List<ItemDetailResponse> findItemDetailResponse(Long itemId);

    void deleteFileOnItem(List<Long> fileIdList);

    void updateItemByDto(Long itemId, EditItemRequest editItemRequest);

    void updateItemByDto(Long itemId, EditItemRequestV2 editItemRequest);

    ResponseEntity<List<CarouselItemResponse>> getCarouselItemsInHome();

    Page<ItemResponse> getItemResponseByCondWhenLastPage(ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable, Long lastItemId, Integer lastPageNumber, Integer pageChunk, Boolean isLastPage);

}
