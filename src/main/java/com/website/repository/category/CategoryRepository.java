package com.website.repository.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.category.Category;
import com.website.domain.category.QCategory;
import com.website.domain.category.QSubcategory;
import com.website.domain.category.Subcategory;
import com.website.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CategoryRepository {

    //여기서 DataAccessException이 올라오지만 어차피 처리 못하므로 Advisor에서 처리하도록 한다.
    private final CategoryJpaRepository categoryJpaRepository;
    private final SubcategoryJpaRepository subcategoryJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;


    public CategoryRepository(EntityManager entityManager, CategoryJpaRepository categoryJpaRepository, SubcategoryJpaRepository subcategoryJpaRepository) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.categoryJpaRepository = categoryJpaRepository;
        this.subcategoryJpaRepository = subcategoryJpaRepository;
    }

    //Create
    public Category saveCategory(Category category) {
        return categoryJpaRepository.save(category);
    }

    public Subcategory saveSubcategory(Subcategory subcategory) {
        if (subcategory.getCategory() == null) {
            return null;
        }

        if (subcategory.getCategory().getId() == null) {
            return null;
        }

        Category category = categoryFindById(subcategory.getCategory().getId());
        if (category == null) {
            return null;
        }
        return subcategoryJpaRepository.save(subcategory);
    }

    //Read
    public Category categoryFindById(Long id) {
        return categoryJpaRepository.findById(id).orElse(null);
    }

    public Subcategory subcategoryFindById(Long id) {
        Optional<Subcategory> byId = subcategoryJpaRepository.findById(id);
        return byId.orElse(null);
    }

    //Update
    public Category updateCategory(Category category) {
        if (category.getId() == null) {
            return null;
        }

        Category findCategory = categoryFindById(category.getId());
        if (findCategory == null) {
            return null;
        }
        return categoryJpaRepository.save(category);
    }

    public Subcategory updateSubcategory(Subcategory subcategory) {
        //확인로직
        Subcategory findSubCategory = subcategoryFindById(subcategory.getId());
        if (findSubCategory == null) {
            return null;
        }
        Long categoryId = subcategory.getCategory().getId();
        Category category = categoryFindById(categoryId);
        if (category == null) {
            return null;
        }

        return subcategoryJpaRepository.save(subcategory);
    }


    //Delete
    public void deleteCategory(Long id) {
        try {
            categoryJpaRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            log.error("delete err", e);
        }
    }

    public void deleteSubcategory(Long id) {
        subcategoryJpaRepository.deleteById(id);
    }

    public List<Category> categoryFindAll() {
        return categoryJpaRepository.findAll();
    }

    public List<Subcategory> subcategoriesFindByCategoryId(Long categoryId) {
        return jpaQueryFactory
                .selectFrom(QSubcategory.subcategory)
                .where(QSubcategory.subcategory.category.id.eq(categoryId))
                .fetch();
    }

    public List<Subcategory> subcategoryFindAll() {
        return subcategoryJpaRepository.findAll();
    }
}
