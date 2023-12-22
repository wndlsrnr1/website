package com.website.repository.category;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.category.Category;
import com.website.domain.category.QCategory;
import com.website.domain.category.QSubcategory;
import com.website.domain.category.Subcategory;
import com.website.web.service.category.CategoryCRUDService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    EntityManager entityManager;

    @Autowired
    CategoryCRUDService categoryCRUDService;
    @Transactional
    @Test
    void createTest() {
        //[[[[[[[[[[[[[[[[[[[[[[[[[[[create]]]]]]]]]]]]]]]]]]]]]]]]]]]

        //[[[[[[[[[[[[[[[[[[정상흐름]]]]]]]]]]]]]]]]]]
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");

        Category createdCategory = categoryRepository.saveCategory(category);
        Subcategory createdSubcategory = categoryRepository.saveSubcategory(subcategory);

        log.info("category = {}", category);
        log.info("subcategory = {}", subcategory);

        assertThat(createdCategory.getId()).isEqualTo(createdSubcategory.getCategory().getId());
        assertThat(category).isEqualTo(createdCategory);
        assertThat(subcategory).isEqualTo(createdSubcategory);

        //[[[[[[[[[[[[[[[[[[[[[[[[[[잘못된 정보]]]]]]]]]]]]]]]]]]]]]]]]]]
        Category categoryFake = new Category("PLAY_STATION2", "플레이스테이션2");
        Subcategory subcategoryFail = new Subcategory(categoryFake, "SUB_PLAY_STATION2", "서브_플스2");
        Subcategory uncreatedSubcategory = categoryRepository.saveSubcategory(subcategoryFail);
        Assertions.assertThat(uncreatedSubcategory).isNull();

        //[[[[[[[[[[[[[[[[[[[[[[[[삭제된 정보]]]]]]]]]]]]]]]]]]]]]]]]
        categoryRepository.deleteCategory(createdCategory.getId());
        Subcategory subcategoryFail2 = new Subcategory(createdCategory, "SUB_PLAY_STATION2", "서브_플스2");
        Subcategory uncreatedSubcategory2 = categoryRepository.saveSubcategory(subcategoryFail2);
        assertThat(uncreatedSubcategory2).isNull();
    }
    @Transactional
    @Test
    void readTest() {
        //given
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");
        Category createdCategory = categoryRepository.saveCategory(category);
        Subcategory createdSubcategory = categoryRepository.saveSubcategory(subcategory);

        //when
        Category category1 = categoryRepository.categoryFindById(createdCategory.getId());
        Subcategory subcategory1 = categoryRepository.subcategoryFindById(createdSubcategory.getId());

        //then 정상 흐름
        assertThat(category).isEqualTo(category1);
        assertThat(subcategory).isEqualTo(subcategory1);

        //then 없는 ID 찾기
        Category expectedNullCategory = categoryRepository.categoryFindById(Long.MIN_VALUE);
        Subcategory expectedNullSubcategory = categoryRepository.subcategoryFindById(Long.MIN_VALUE);
        assertThat(expectedNullCategory).isNull();
        assertThat(expectedNullSubcategory).isNull();

        categoryRepository.deleteCategory(createdCategory.getId());
        Subcategory expectedNullSubcategory2 = categoryRepository.subcategoryFindById(createdCategory.getId());
        assertThat(expectedNullSubcategory2).isNull();
    }
    @Transactional
    @Test
    void deleteTest() {
        //given
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");
        Category createdCategory = categoryRepository.saveCategory(category);
        Subcategory createdSubcategory = categoryRepository.saveSubcategory(subcategory);

        //when
        categoryRepository.deleteSubcategory(createdSubcategory.getId());
        categoryRepository.deleteCategory(createdCategory.getId());

        //then
        Category expectedNull1 = categoryRepository.categoryFindById(category.getId());
        Subcategory expectedNull2 = categoryRepository.subcategoryFindById(subcategory.getId());
        assertThat(expectedNull1).isNull();
        assertThat(expectedNull2).isNull();
    }

    @Transactional
    @Test
    void updateTest() {
        //given
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");
        Category createdCategory = categoryRepository.saveCategory(category);
        Subcategory createdSubcategory = categoryRepository.saveSubcategory(subcategory);
        String categoryName = category.getName();
        String subcategoryName = subcategory.getName();

        //when
        Category forUpdateDto = new Category("XBOX", "엑스박스");
        forUpdateDto.setId(createdCategory.getId());
        Subcategory forUpdateDtoSub = new Subcategory(forUpdateDto, "SUB_XBOX", "서브 엑박");
        forUpdateDtoSub.setId(createdSubcategory.getId());
        Category updatedCategory = categoryRepository.updateCategory(forUpdateDto);
        Subcategory updateSubcategory = categoryRepository.updateSubcategory(forUpdateDtoSub);

        //같은 객체로 반환해줘서 다른지 확인 안 됨. 왜??
        //then
        assertThat(createdCategory).isEqualTo(updatedCategory);
        assertThat(createdSubcategory).isEqualTo(updateSubcategory);

        //다른 값에 저장
        assertThat(categoryName).isNotEqualTo(updatedCategory.getName());
        assertThat(subcategoryName).isNotEqualTo(updateSubcategory.getName());
    }

    @Test
    void CategorySubcategoryDeleteCascadeTest() {
        //given
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Category createdCategory = categoryRepository.saveCategory(category);
        Subcategory subcategory = new Subcategory(createdCategory, "SUB_PLAY_STATION", "서브_플스");
        Subcategory createdSubcategory = categoryRepository.saveSubcategory(subcategory);

        //상위 삭제
        categoryRepository.deleteCategory(createdCategory.getId());
        Subcategory subcategory1 = categoryRepository.subcategoryFindById(createdSubcategory.getId());
        assertThat(categoryRepository.categoryFindById(createdCategory.getId())).isNull();
        assertThat(categoryRepository.subcategoryFindById(createdSubcategory.getId())).isNull();
    }


    @Test
    void joinTest() {
        List<Category> categories = categoryRepository.categoryFindAll();
        log.info("categories = {}", categories);

    }

}