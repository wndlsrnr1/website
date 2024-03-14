package com.website.repository.item.thumbnail;

import com.website.domain.item.ItemThumbnail;

public interface ItemThumbnailCustomRepository {
    void updateByItemIdAndAttachmentId(Long itemId, Long thumbnailId);

    ItemThumbnail findByItemId(Long itemId);

    void deleteByItemId(Long itemId);
}
