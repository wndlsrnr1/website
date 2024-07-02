package com.website.repository.item.seq;

import com.website.domain.item.ItemAttachmentSeq;
import com.website.web.dto.response.item.sequence.ItemAttachmentSequenceResponse;

import java.util.List;

public interface ItemAttachmentSeqCustomRepository {
    void update(Long itemAttachmentId, Integer sequence);

    void deleteByItemAttachmentId(Long id);

    List<ItemAttachmentSequenceResponse> findByItemId(Long itemId);
}
