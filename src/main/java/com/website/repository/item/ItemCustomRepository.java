package com.website.repository.item;

import com.website.web.dto.response.item.ItemResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomRepository {
    void updateNameById(String updateName, Long id);

    Page<ItemResponse> getItemResponseByCond(ItemSearchCond itemSearchCond, Pageable pageable);

}
