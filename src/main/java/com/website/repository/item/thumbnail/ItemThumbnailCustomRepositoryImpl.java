package com.website.repository.item.thumbnail;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.ItemThumbnail;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.website.domain.item.QItemThumbnail.itemThumbnail;

@Repository
public class ItemThumbnailCustomRepositoryImpl implements ItemThumbnailCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public ItemThumbnailCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public void updateItemThumbnail(Long itemThumbnailId, Long attachmentIdParam, Long itemIdParam) {
        if (itemThumbnailId == null) {
            query.insert(itemThumbnail)
                    .columns(itemThumbnail.attachment.id, itemThumbnail.item.id)
                    .values(attachmentIdParam, itemIdParam)
                    .execute();
        }

        query.update(itemThumbnail)
                .set(itemThumbnail.attachment.id, attachmentIdParam)
                .set(itemThumbnail.item.id, itemIdParam)
                .where(itemThumbnail.id.eq(itemThumbnailId))
                .execute();
    }

}
