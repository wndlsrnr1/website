package com.website.repository.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.category.Category;
import com.website.domain.category.QSubcategory;
import com.website.domain.category.Subcategory;
import com.website.repository.subcategory.SubcategoryCustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    //여기서 DataAccessException이 올라오지만 어차피 처리 못하므로 Advisor에서 처리하도록 한다.
    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    public CategoryCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

}
