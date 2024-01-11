package com.website.repository.subcategory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.category.QSubcategory;
import com.website.domain.category.Subcategory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.website.domain.category.QSubcategory.*;

public class SubcategoryCustomRepositoryImpl implements SubcategoryCustomRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public SubcategoryCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Subcategory> findByCategoryId(Long categoryId) {
        return query
                .selectFrom(subcategory)
                .where(subcategory.category.id.eq(categoryId))
                .fetch();
    }

    @Override
    public List<Subcategory> findAll(int limit) {
        return query
                .selectFrom(subcategory)
                .orderBy(subcategory.category.id.asc())
                .limit(limit)
                .fetch();
    }

}
