package com.website.repository.item.info;

import com.website.domain.item.ItemInfo;
import com.website.web.dto.request.item.info.ItemInfoAddRequest;
import com.website.web.dto.request.item.info.ItemInfoEditRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInfoCustomRepository {

    void editItemInfo(ItemInfoEditRequest itemInfoEditRequest);

    void deleteItemInfoByItemId(Long itemId);
}
