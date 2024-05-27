package com.website.repository.item.thumbnail;

import com.website.domain.attachment.Attachment;
import com.website.domain.item.Item;
import com.website.domain.item.ItemThumbnail;

public interface ItemThumbnailCustomRepository {

    ItemThumbnail findByItemId(Long itemId);

    void updateThumbnail(Long itemId, Long imageId);

    void insertItemThumbnail(Attachment attachment, Item item);
}
