package com.website.repository.item;

import com.website.repository.model.item.ItemAttachment;

import java.util.List;

public interface ItemAttachmentCustomRepository {
    void deleteByItemId(Long itemId);

    ItemAttachment findByItemIdAndAttachmentId(Long itemId, Long attachmentId);

    List<Long> findAttachmentIdByItemId(Long itemId);

    List<ItemAttachment> findByAttachmentId(Long attachmentIdForDelete, Long itemId);

    void deleteByAttachmentId(Long attachmentIdListForDelete);
}
