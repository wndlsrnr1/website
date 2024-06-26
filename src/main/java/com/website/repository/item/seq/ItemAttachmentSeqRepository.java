package com.website.repository.item.seq;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAttachmentSeqRepository extends JpaRepository<ItemAttachmentSeqRepository, Long>, ItemAttachmentSeqCustomRepository{
}
