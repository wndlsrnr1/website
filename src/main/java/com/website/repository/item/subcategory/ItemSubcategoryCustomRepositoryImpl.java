package com.website.repository.item.subcategory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.website.domain.item.QItemSubcategory.*;

@Repository
public class ItemSubcategoryCustomRepositoryImpl implements ItemSubcategoryCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public ItemSubcategoryCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public void deleteByItemId(Long itemId) {
        query.delete(itemSubcategory).where(itemSubcategory.item.id.eq(itemId)).execute();
    }

    @Override
    public void updateSubcategory(Long itemId, Long subcategoryId) {
        query.update(itemSubcategory)
                .set(itemSubcategory.subcategory.id, subcategoryId)
                .where(itemSubcategory.item.id.eq(subcategoryId))
                .execute();
    }
}
