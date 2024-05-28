package com.website.repository.item;

import com.website.domain.item.ItemAttachment;

import java.util.List;

public interface ItemAttachmentCustomRepository {
    void deleteByItemId(Long itemId);

    ItemAttachment findByItemIdAndAttachmentId(Long itemId, Long attachmentId);

    List<Long> findAttachmentIdByItemId(Long itemId);
}
