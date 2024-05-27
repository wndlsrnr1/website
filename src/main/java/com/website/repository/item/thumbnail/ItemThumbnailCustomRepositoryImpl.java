package com.website.repository.item.thumbnail;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.attachment.Attachment;
import com.website.domain.item.Item;
import com.website.domain.item.ItemThumbnail;
import com.website.domain.item.QItemThumbnail;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

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
    public ItemThumbnail findByItemId(Long itemId) {
        List<ItemThumbnail> result = query
                .select(itemThumbnail).from(itemThumbnail)
                .where(itemThumbnail.item.id.eq(itemId))
                .fetch();
        if (result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public void updateThumbnail(Long itemId, Long imageId) {
        query.update(itemThumbnail)
                .set(itemThumbnail.attachment.id, imageId)
                .where(itemThumbnail.item.id.eq(itemId))
                .execute();
    }

    @Override
    public void insertItemThumbnail(Attachment attachment, Item item) {
        query.insert(itemThumbnail)
                .columns(itemThumbnail.attachment.id, itemThumbnail.item.id)
                .values(attachment.getId(), item.getId())
                .execute();
    }

    //@Override
    //public void updateItemThumbnail(Long itemThumbnailId, Long attachmentIdParam, Long itemIdParam) {
    //    if (itemThumbnailId == null) {
    //        query.insert(itemThumbnail)
    //                .columns(itemThumbnail.attachment.id, itemThumbnail.item.id)
    //                .values(attachmentIdParam, itemIdParam)
    //                .execute();
    //    }
    //
    //    query.update(itemThumbnail)
    //            .set(itemThumbnail.attachment.id, attachmentIdParam)
    //            .set(itemThumbnail.item.id, itemIdParam)
    //            .where(itemThumbnail.id.eq(itemThumbnailId))
    //            .execute();
    //}

}
