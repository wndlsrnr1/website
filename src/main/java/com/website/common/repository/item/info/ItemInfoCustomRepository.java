package com.website.common.repository.item.info;

import com.website.common.repository.item.info.model.ItemInfoEditRequest;
import com.website.common.repository.item.model.ItemInfoResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInfoCustomRepository {

    void editItemInfo(ItemInfoEditRequest itemInfoEditRequest);

    void deleteItemInfoByItemId(Long itemId);

    void updateItemInfo(String madeIn, String brand, Integer saleRate, String manufacturer, Long itemId);

    ItemInfoResponse findByItemId(Long itemId);
}
