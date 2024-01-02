package com.website.repository.item;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.domain.item.Item;
import com.website.repository.category.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemJpaRepository itemJpaRepository;


    @Test
    public void 널아님() {
        Assertions.assertThat(itemRepository).isNotNull();
    }

    //Create

    @Test
    void create() {
        //given
        Category category = categoryRepository.saveCategory(new Category("name", "nameKor"));
        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        categoryRepository.saveSubcategory(subcategory);
        Subcategory sub = categoryRepository.subcategoryFindById(subcategory.getId());

        Item item = Item.builder()
                .name("이름")
                .nameKor("이름")
                .price(100000)
                .quantity(10000)
                .description("asdf")
                .releaseDate(LocalDateTime.now())
                .status("good")
                .subcategory(sub)
                .build();

        //when
        itemRepository.saveItem(item);

        //then
        Item findItem = itemRepository.findItemById(item.getId());
        log.info("[[[saved item id]]] = {}", findItem.getId());
        Assertions.assertThat(findItem).isNotNull();
        Assertions.assertThat(item.getName()).isEqualTo(findItem.getName());
    }

    //Read
    @Transactional
    @Test
    void read() {
        //given
        Category category = categoryRepository.saveCategory(new Category("name", "nameKor"));
        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        categoryRepository.saveSubcategory(subcategory);
        Subcategory sub = categoryRepository.subcategoryFindById(subcategory.getId());

        for (int i = 0; i < 100; i++) {
            Item item = Item.builder()
                    .name("name" + i)
                    .nameKor("이름" + i)
                    .price(100000 + i)
                    .quantity(10000 + i)
                    .description("asdf")
                    .releaseDate(LocalDateTime.now())
                    .status("good")
                    .subcategory(sub)
                    .build();

            itemRepository.saveItem(item);
        }

        List<Item> items = itemRepository.findAll();

        log.info("items.stream().findAny().get() = {}", items.stream().findAny().get().getName());
        Assertions.assertThat(items).isNotNull();
        Assertions.assertThat(items.stream().findAny().get()).isNotNull();
    }


    //Update
    @Test
    void update() {
        //given
        //given
        Category category = categoryRepository.saveCategory(new Category("name", "nameKor"));
        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        categoryRepository.saveSubcategory(subcategory);
        Subcategory sub = categoryRepository.subcategoryFindById(subcategory.getId());

        Item item = Item.builder()
                .name("이름")
                .nameKor("이름")
                .price(100000)
                .quantity(10000)
                .description("asdf")
                .releaseDate(LocalDateTime.now())
                .status("good")
                .subcategory(sub)
                .build();

        //when
        itemRepository.saveItem(item);

        //then
        Item findItem = itemRepository.findItemById(item.getId());

        //when
        itemRepository.updateNameById("업데이트", findItem.getId());

        //then
        Item itemById = itemRepository.findItemById(findItem.getId());
        log.info("name = {}", itemById.getName());

    }


    //Delete
    @Test
    void delete() {
        Category category = categoryRepository.saveCategory(new Category("name", "nameKor"));
        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        categoryRepository.saveSubcategory(subcategory);
        Subcategory sub = categoryRepository.subcategoryFindById(subcategory.getId());

        Item item = Item.builder()
                .name("이름")
                .nameKor("이름")
                .price(100000)
                .quantity(10000)
                .description("asdf")
                .releaseDate(LocalDateTime.now())
                .status("good")
                .subcategory(sub)
                .build();

        itemRepository.saveItem(item);

        itemRepository.deleteById(item.getId());

        Item itemById = itemRepository.findItemById(item.getId());
        log.info("item.getId() = {}", item.getId());
        Assertions.assertThat(itemById).isNull();
    }
}