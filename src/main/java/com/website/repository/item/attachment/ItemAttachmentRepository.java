package com.website.repository.item.attachment;

import com.website.domain.item.ItemAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAttachmentRepository extends JpaRepository<ItemAttachment, Long>, ItemAttachmentCustomRepository {

}
