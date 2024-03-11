package com.website.repository.item.attachment;

import com.website.domain.item.ItemAttachment;

public interface ItemAttachmentCustomRepository {
    void deleteByItemId(Long itemId);

    ItemAttachment findByItemIdAndAttachmentId(Long itemId, Long attachmentId);
}
