package com.website.repository.item.thumbnail;

import com.website.domain.item.ItemThumbnail;

public interface ItemThumbnailCustomRepository {

    ItemThumbnail findByItemId(Long itemId);
}
