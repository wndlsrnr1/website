package com.website.repository.category;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.service.category.CategoryCRUDService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    CategoryCRUDService categoryCRUDService;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Transactional
    @Test
    void createTest() {

        //[[[[[[[[[[[[[[[[[[[[[[[[[[[create]]]]]]]]]]]]]]]]]]]]]]]]]]]

        //[[[[[[[[[[[[[[[[[[정상흐름]]]]]]]]]]]]]]]]]]
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");

        Category createdCategory = categoryRepository.save(category);
        Subcategory createdSubcategory = subcategoryRepository.save(subcategory);

        log.info("category = {}", category);
        log.info("subcategory = {}", subcategory);

        assertThat(createdCategory.getId()).isEqualTo(createdSubcategory.getCategory().getId());
        assertThat(category).isEqualTo(createdCategory);
        assertThat(subcategory).isEqualTo(createdSubcategory);


        //[[[[[[[[[[[[[[[[[[[[[[[[삭제된 정보]]]]]]]]]]]]]]]]]]]]]]]]
        categoryRepository.deleteById(createdCategory.getId());

        //Subcategory subcategoryFail2 = new Subcategory(createdCategory, "SUB_PLAY_STATION2", "서브_플스2");
        //Subcategory uncreatedSubcategory2 = subcategoryRepository.save(subcategoryFail2);
        //assertThat(uncreatedSubcategory2).isNull();

    }

    @Transactional
    @Test
    void readTest() {
        //given
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");
        Category createdCategory = categoryRepository.save(category);
        Subcategory createdSubcategory = subcategoryRepository.save(subcategory);

        //when
        Category category1 = categoryRepository.findById(createdCategory.getId()).get();
        Subcategory subcategory1 = subcategoryRepository.findById(createdSubcategory.getId()).get();

        //then 정상 흐름
        assertThat(category).isEqualTo(category1);
        assertThat(subcategory).isEqualTo(subcategory1);

        //then 없는 ID 찾기
        boolean empty = categoryRepository.findById(Long.MIN_VALUE).isEmpty();
        Subcategory expectedNullSubcategory = subcategoryRepository.findById(Long.MIN_VALUE).orElse(null);
        assertThat(empty).isTrue();
        assertThat(expectedNullSubcategory).isNull();

        categoryRepository.deleteById(createdCategory.getId());
        Subcategory expectedNullSubcategory2 = subcategoryRepository.findById(createdCategory.getId()).orElse(null);
        assertThat(expectedNullSubcategory2).isNull();
    }

    @Transactional
    @Test
    void deleteTest() {
        //given
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");
        Category createdCategory = categoryRepository.save(category);
        Subcategory createdSubcategory = subcategoryRepository.save(subcategory);

        //when
        subcategoryRepository.deleteById(createdSubcategory.getId());
        categoryRepository.deleteById(createdCategory.getId());

        //then
        Category expectedNull1 = categoryRepository.findById(category.getId()).orElse(null);
        Subcategory expectedNull2 = subcategoryRepository.findById(subcategory.getId()).orElse(null);

        assertThat(expectedNull1).isNull();
        assertThat(expectedNull2).isNull();
    }

    @Transactional
    @Test
    void updateTest() {
        //given
        Category category = new Category("PLAY_STATION", "플레이스테이션");
        Subcategory subcategory = new Subcategory(category, "SUB_PLAY_STATION", "서브_플스");
        Category createdCategory = categoryRepository.save(category);
        Subcategory createdSubcategory = subcategoryRepository.save(subcategory);
        String categoryName = category.getName();
        String subcategoryName = subcategory.getName();

        //when
        Category forUpdateDto = new Category("XBOX", "엑스박스");
        forUpdateDto.setId(createdCategory.getId());
        Subcategory forUpdateDtoSub = new Subcategory(forUpdateDto, "SUB_XBOX", "서브 엑박");
        forUpdateDtoSub.setId(createdSubcategory.getId());

        Category updatedCategory = categoryRepository.save(forUpdateDto);
        Subcategory updateSubcategory = subcategoryRepository.save(forUpdateDtoSub);

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
        Category createdCategory = categoryRepository.save(category);
        Subcategory subcategory = new Subcategory(createdCategory, "SUB_PLAY_STATION", "서브_플스");
        Subcategory createdSubcategory = subcategoryRepository.save(subcategory);

        //상위 삭제
        categoryRepository.deleteById(createdCategory.getId());
        Subcategory subcategory1 = subcategoryRepository.findById(createdSubcategory.getId()).orElse(null);
        assertThat(categoryRepository.findById(createdCategory.getId()).orElse(null)).isNull();
        assertThat(subcategoryRepository.findById(createdSubcategory.getId()).orElse(null)).isNull();

    }


    @Test
    void joinTest() {
        List<Category> categories = categoryRepository.findAll();
        log.info("categories = {}", categories);
    }

}