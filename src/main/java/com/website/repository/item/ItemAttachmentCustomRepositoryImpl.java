package com.website.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.repository.model.item.ItemAttachment;
import com.website.repository.model.item.QItemAttachment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.website.repository.model.item.QItemAttachment.itemAttachment;


@Repository
public class ItemAttachmentCustomRepositoryImpl implements ItemAttachmentCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public ItemAttachmentCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public void deleteByItemId(Long itemId) {
        query.delete(itemAttachment).where(itemAttachment.item.id.eq(itemId)).execute();
    }

    @Override
    public ItemAttachment findByItemIdAndAttachmentId(Long itemId, Long attachmentId) {
        return query.selectFrom(QItemAttachment.itemAttachment)
                .where(QItemAttachment.itemAttachment.item.id.eq(itemId), QItemAttachment.itemAttachment.attachment.id.eq(attachmentId))
                .fetchFirst();
    }

    @Override
    public List<Long> findAttachmentIdByItemId(Long itemId) {
        return query
                .select(itemAttachment.attachment.id)
                .from(itemAttachment)
                .where(itemAttachment.item.id.eq(itemId))
                .fetch();
    }

    @Override
    public List<ItemAttachment> findByAttachmentId(Long attachmentId, Long itemId) {
        return query
                .select(itemAttachment)
                .from(itemAttachment)
                .where(itemAttachment.attachment.id.eq(attachmentId).and(itemAttachment.item.id.eq(itemId)))
                .fetch();
    }

    @Override
    public void deleteByAttachmentId(Long attachmentIdListForDelete) {
        query.delete(itemAttachment)
                .where(itemAttachment.attachment.id.eq(attachmentIdListForDelete)).execute();
        return;
    }
}
