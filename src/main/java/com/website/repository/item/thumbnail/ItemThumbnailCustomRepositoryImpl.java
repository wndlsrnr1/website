package com.website.repository.item.thumbnail;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.ItemThumbnail;
import com.website.domain.item.QItemThumbnail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

import static com.website.domain.item.QItemThumbnail.itemThumbnail;

@Slf4j
@RequiredArgsConstructor
public class ItemThumbnailCustomRepositoryImpl implements ItemThumbnailCustomRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    @Override
    public void updateByItemIdAndAttachmentId(Long itemId, Long thumbnailId) {
        log.info("itemId = {}, thumbnailId = {}", itemId, thumbnailId);
        query.update(itemThumbnail)
                .set(itemThumbnail.attachment.id, thumbnailId)
                .where(itemThumbnail.item.id.eq(itemId))
                .execute();
    }

    @Override
    public ItemThumbnail findByItemId(Long itemId) {
        return query.selectFrom(itemThumbnail).where(itemThumbnail.item.id.eq(itemId)).fetchOne();
    }

    @Override
    public void deleteByItemId(Long itemId) {
        query.delete(itemThumbnail).where(itemThumbnail.item.id.eq(itemId)).execute();
    }

    //CRUD

    //Delete시에 해야할 것 기존에 있던 썸네일 삭제해야함.

}
