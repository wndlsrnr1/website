package com.website.repository.item.seq;

import com.website.controller.api.model.response.item.sequence.ItemAttachmentSequenceResponse;

import java.util.List;

public interface ItemAttachmentSeqCustomRepository {
    void update(Long itemAttachmentId, Integer sequence);

    void deleteByItemAttachmentId(Long id);

    List<ItemAttachmentSequenceResponse> findByItemId(Long itemId);
}
