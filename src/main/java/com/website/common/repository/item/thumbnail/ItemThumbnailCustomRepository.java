package com.website.common.repository.item.thumbnail;

import com.website.common.repository.model.attachment.Attachment;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.item.model.ItemThumbnailResponse;

public interface ItemThumbnailCustomRepository {

    ItemThumbnailResponse findByItemIdV2(Long itemId);

    void updateThumbnail(Long itemId, Long imageId);

    void insertItemThumbnail(Attachment attachment, Item item);

    void deleteByItemId(Long itemId);

    void insertItemThumbnail(Long itemId, Long imageId);
}
