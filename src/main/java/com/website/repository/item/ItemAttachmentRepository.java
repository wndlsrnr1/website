package com.website.repository.item;

import com.website.repository.model.item.ItemAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAttachmentRepository extends JpaRepository<ItemAttachment, Long>, ItemAttachmentCustomRepository {

}
