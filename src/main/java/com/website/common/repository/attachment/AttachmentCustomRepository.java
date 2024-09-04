package com.website.common.repository.attachment;

import com.website.common.repository.model.attachment.Attachment;
import com.website.common.repository.attachment.model.ItemImageInfoForCustomerResponse;

import java.util.List;

public interface AttachmentCustomRepository {
    List<Attachment> findByRequestName(String requestName);

    List<Attachment> findBySaveName(String saveName);

    List<Attachment> saveAttachments(List<Attachment> attachments);

    Long deleteAttachmentBySaveName(String saveName);

    List<Attachment> getAttachmentByItemId(Long itemId);

    List<Attachment> getListByIdList(List<Long> fileIdList);

    List<ItemImageInfoForCustomerResponse> getResponseImageInfoForCustomerResponse(Long itemId);
}
