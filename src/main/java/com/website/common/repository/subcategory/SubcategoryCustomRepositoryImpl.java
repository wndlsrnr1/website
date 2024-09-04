package com.website.common.repository.subcategory;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.repository.category.model.QSubcategoryByCondResponse;
import com.website.common.repository.model.category.Subcategory;
import com.website.common.repository.category.model.SubcategoryByCondResponse;
import com.website.common.controller.category.model.SubCategorySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.website.common.repository.model.category.QSubcategory.subcategory;


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

    @Override
    public Page<SubcategoryByCondResponse> searchPageByCond(SubCategorySearchCond subCategorySearchCond, Pageable pageable) {
        List<SubcategoryByCondResponse> element = query
                .select(
                        new QSubcategoryByCondResponse(
                                subcategory.category.id,
                                subcategory.id,
                                subcategory.name,
                                subcategory.nameKor
                        )
                )
                .from(subcategory)
                .where(categoryIdEq(subCategorySearchCond.getCategoryId()), nameOrNameKorLike(subCategorySearchCond.getSearchName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.selectFrom(subcategory)
                .where(categoryIdEq(subCategorySearchCond.getCategoryId()), nameOrNameKorLike(subCategorySearchCond.getSearchName()))
                .fetchCount();

        return new PageImpl<>(element, pageable, total);
    }

    private BooleanExpression nameOrNameKorLike(String searchNameCond) {
        if (!StringUtils.hasText(searchNameCond)) {
            return null;
        }
        return nameKorLike(searchNameCond).or(nameLike(searchNameCond));
    }

    private BooleanExpression categoryIdEq(Long categoryIdCond) {
        if (categoryIdCond == null) {
            return null;
        }
        return subcategory.category.id.eq(categoryIdCond);
    }

    private BooleanExpression nameLike(String nameCond) {
        if (!StringUtils.hasText(nameCond)) {
            return null;
        }
        return subcategory.name.contains(nameCond);
    }

    private BooleanExpression nameKorLike(String nameKorCond) {
        if (!StringUtils.hasText(nameKorCond)) {
            return null;
        }
        return subcategory.nameKor.contains(nameKorCond);
    }

}
