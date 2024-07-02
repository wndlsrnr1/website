package com.website.repository.item.info;

import com.website.domain.item.ItemInfo;
import com.website.web.dto.request.item.info.ItemInfoEditRequest;
import com.website.web.dto.response.item.ItemInfoResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInfoCustomRepository {

    void editItemInfo(ItemInfoEditRequest itemInfoEditRequest);

    void deleteItemInfoByItemId(Long itemId);

    void updateItemInfo(String madeIn, String brand, Integer saleRate, String manufacturer, Long itemId);

    ItemInfoResponse findByItemId(Long itemId);
}
