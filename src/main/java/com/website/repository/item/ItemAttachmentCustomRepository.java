package com.website.repository.item;

import com.website.domain.item.ItemAttachment;

public interface ItemAttachmentCustomRepository {
    void deleteByItemId(Long itemId);

    ItemAttachment findByItemIdAndAttachmentId(Long itemId, Long attachmentId);
}
