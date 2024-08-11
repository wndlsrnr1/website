package com.website.repository.item.seq;

import com.website.repository.model.item.ItemAttachmentSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAttachmentSeqRepository extends JpaRepository<ItemAttachmentSeq, Long>, ItemAttachmentSeqCustomRepository {

}
