package com.website.common.repository.item.seq;

import com.website.common.repository.model.item.ItemAttachmentSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAttachmentSeqRepository extends JpaRepository<ItemAttachmentSeq, Long>, ItemAttachmentSeqCustomRepository {

}
