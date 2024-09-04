package com.website.common.repository.item.seq;

import com.website.common.repository.item.model.ItemAttachmentSequenceResponse;

import java.util.List;

public interface ItemAttachmentSeqCustomRepository {
    void update(Long itemAttachmentId, Integer sequence);

    void deleteByItemAttachmentId(Long id);

    List<ItemAttachmentSequenceResponse> findByItemId(Long itemId);
}
