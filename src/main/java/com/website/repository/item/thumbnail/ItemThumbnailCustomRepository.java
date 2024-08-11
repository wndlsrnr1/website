package com.website.repository.item.thumbnail;

import com.website.repository.model.attachment.Attachment;
import com.website.repository.model.item.Item;
import com.website.controller.api.model.response.item.ItemThumbnailResponse;

public interface ItemThumbnailCustomRepository {

    ItemThumbnailResponse findByItemId(Long itemId);

    void updateThumbnail(Long itemId, Long imageId);

    void insertItemThumbnail(Attachment attachment, Item item);

    void deleteByItemId(Long itemId);

    void insertItemThumbnail(Long itemId, Long imageId);
}
