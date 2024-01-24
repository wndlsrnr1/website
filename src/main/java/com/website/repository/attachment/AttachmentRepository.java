package com.website.repository.attachment;

import com.website.domain.attachment.Attachment;

import java.util.List;

public interface AttachmentRepository {

    Attachment findById(Long id);

    List<Attachment> findByRequestName(String requestName);

    List<Attachment> findBySaveName(String saveName);

    Attachment saveAttachment(String requestName, String saveName);

    Attachment saveAttachment(Attachment attachmentParam);

    List<Attachment> saveAttachments(List<Attachment> attachments);

    boolean deleteAttachmentById(Long id);

    Long deleteAttachmentBySaveName(String saveName);
}
