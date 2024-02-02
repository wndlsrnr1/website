package com.website.repository.item;

import com.website.web.dto.request.item.EditItemRequest;
import com.website.web.dto.response.item.ItemDetailResponse;
import com.website.web.dto.response.item.ItemResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemCustomRepository {
    void updateNameById(String updateName, Long id);

    Page<ItemResponse> getItemResponseByCond(ItemSearchCond itemSearchCond, Pageable pageable);

    List<ItemDetailResponse> findItemDetailResponse(Long itemId);

    void deleteFileOnItem(List<Long> fileIdList);

    void updateItemByDto(Long itemId, EditItemRequest editItemRequest);
}
