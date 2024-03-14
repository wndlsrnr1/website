package com.website.repository.item.attachment;

import com.website.domain.item.ItemAttachment;

import java.util.List;

public interface ItemAttachmentCustomRepository {
    void deleteByItemId(Long itemId);

    ItemAttachment findByItemIdAndAttachmentId(Long itemId, Long attachmentId);

    List<ItemAttachment> findByItemId(Long itemId);
}
