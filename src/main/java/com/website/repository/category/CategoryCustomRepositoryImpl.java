package com.website.repository.category;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.web.dto.response.category.CategoryByCondResponse;
import com.website.web.dto.response.category.QCategoryByCondResponse;
import com.website.web.dto.sqlcond.category.CategorySearchCond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.website.domain.category.QCategory.category;

@Slf4j
@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    //여기서 DataAccessException이 올라오지만 어차피 처리 못하므로 Advisor에서 처리하도록 한다.
    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public CategoryCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<CategoryByCondResponse> searchPageByCond(CategorySearchCond categorySearchCond, Pageable pageable) {

        //그냥 쿼리를 전송
        List<CategoryByCondResponse> elements = query
                .select(new QCategoryByCondResponse(category.id, category.name, category.nameKor))
                .from(category)
                .where(nameOrKorNameLike(categorySearchCond.getSearchName()))
                .offset(pageable.getOffset())
                .limit(Math.min(pageable.getPageSize(), 100))
                .orderBy(category.name.asc())
                .fetch();

        //fetch로 전체만 찾기
        long total = query.select(category)
                .from(category)
                .where(nameOrKorNameLike(categorySearchCond.getSearchName()))
                .fetchCount();

        return new PageImpl<>(elements, pageable, total);
    }

    private BooleanExpression nameOrKorNameLike(String searchName) {
        log.info(searchName);
        return searchName == null ? nameKorLike(null) : nameKorLike(searchName).or(nameLike(searchName));
    }

    private BooleanExpression nameKorLike(String searchName) {
        return !StringUtils.hasText(searchName) ? null : category.nameKor.contains(searchName);
    }

    private BooleanExpression nameLike(String searchName) {
        return !StringUtils.hasText(searchName) ? null : category.name.contains(searchName);
    }
}
