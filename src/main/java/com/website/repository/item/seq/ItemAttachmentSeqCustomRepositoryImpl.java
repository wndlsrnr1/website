package com.website.repository.item.seq;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.controller.api.model.response.item.sequence.ItemAttachmentSequenceResponse;
import com.website.controller.api.model.response.item.sequence.QItemAttachmentSequenceResponse;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.website.repository.model.item.QItemAttachment.itemAttachment;
import static com.website.repository.model.item.QItemAttachmentSeq.itemAttachmentSeq;


@Repository
public class ItemAttachmentSeqCustomRepositoryImpl implements ItemAttachmentSeqCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public ItemAttachmentSeqCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public void update(Long itemAttachmentId, Integer sequence) {
        query.update(itemAttachmentSeq)
                .set(itemAttachmentSeq.seq, sequence)
                .where(itemAttachmentSeq.itemAttachment.id.eq(itemAttachmentId)).execute();
    }

    @Override
    public void deleteByItemAttachmentId(Long itemAttachmentId) {
        query.delete(itemAttachmentSeq)
                .where(itemAttachmentSeq.itemAttachment.id.eq(itemAttachmentId))
                .execute();
    }

    @Override
    public List<ItemAttachmentSequenceResponse> findByItemId(Long itemId) {
        List<ItemAttachmentSequenceResponse> fetch = query.select(new QItemAttachmentSequenceResponse(itemAttachment.attachment.id, itemAttachmentSeq.seq))
                .from(itemAttachmentSeq)
                .join(itemAttachment)
                .on(itemAttachmentSeq.itemAttachment.id.eq(itemAttachment.id))
                .where(itemAttachment.item.id.eq(itemId))
                .fetch();
        return fetch;
    }
}
