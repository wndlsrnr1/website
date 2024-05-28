package com.website.repository.item.thumbnail;

import com.website.domain.attachment.Attachment;
import com.website.domain.item.Item;
import com.website.domain.item.ItemThumbnail;
import com.website.web.dto.response.item.ItemThumbnailResponse;

public interface ItemThumbnailCustomRepository {

    ItemThumbnailResponse findByItemId(Long itemId);

    void updateThumbnail(Long itemId, Long imageId);

    void insertItemThumbnail(Attachment attachment, Item item);

    void deleteByItemId(Long itemId);

    void insertItemThumbnail(Long itemId, Long imageId);
}
