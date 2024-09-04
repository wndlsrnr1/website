package com.website.common.repository.item;

import com.website.common.repository.model.item.ItemAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAttachmentRepository extends JpaRepository<ItemAttachment, Long>, ItemAttachmentCustomRepository {

}
