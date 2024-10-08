package com.website.repository.item;

import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.model.category.Category;
import com.website.common.repository.model.category.Subcategory;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.category.CategoryRepository;
import com.website.common.repository.subcategory.SubcategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    //@Autowired
    //EntityManager entityManager;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 널아님() {
        Assertions.assertThat(itemRepository).isNotNull();
    }

    //Create

    @Test
    void create() throws NoSuchFieldException {
        //given
        Category category = new Category("name", "nameKor");
        categoryRepository.save(category);

        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        subcategoryRepository.save(subcategory);

        Item item = Item.builder()
                .name("이름")
                .nameKor("이름")
                .price(100000)
                .quantity(10000)
                .description("asdf")
                .releasedAt(LocalDateTime.now())
                .status("good")
                .build();

        itemRepository.save(item);
        log.info("item = {}", item);
    }

    //Read
    @Transactional
    @Test
    void read() {
        //given
        Category category = categoryRepository.save(new Category("name", "nameKor"));
        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        subcategoryRepository.save(subcategory);
        Subcategory sub = subcategoryRepository.findById(subcategory.getId()).orElseGet(null);

        for (int i = 0; i < 100; i++) {
            Item item = Item.builder()
                    .name("name" + i)
                    .nameKor("이름" + i)
                    .price(100000 + i)
                    .quantity(10000 + i)
                    .description("asdf")
                    .releasedAt(LocalDateTime.now())
                    .status("good")
                    .build();

            itemRepository.save(item);
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
        Category category = categoryRepository.save(new Category("name", "nameKor"));
        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        subcategoryRepository.save(subcategory);
        Subcategory sub = subcategoryRepository.findById(subcategory.getId()).orElseGet(null);

        Item item = Item.builder()
                .name("이름")
                .nameKor("이름")
                .price(100000)
                .quantity(10000)
                .description("asdf")
                .releasedAt(LocalDateTime.now())
                .status("good")
                .build();

        //when
        itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId()).get();

        //when
        itemRepository.updateNameById("업데이트", findItem.getId());

        //then
        Item itemById = itemRepository.findById(findItem.getId()).get();
        log.info("name = {}", itemById.getName());

    }


    //Delete
    @Test
    void delete() {
        Category category = categoryRepository.save(new Category("name", "nameKor"));
        Subcategory subcategory = new Subcategory(category, "name", "nameKor");
        subcategoryRepository.save(subcategory);
        Subcategory sub = subcategoryRepository.findById(subcategory.getId()).orElseGet(null);

        Item item = Item.builder()
                .name("이름")
                .nameKor("이름")
                .price(100000)
                .quantity(10000)
                .description("asdf")
                .releasedAt(LocalDateTime.now())
                .status("good")
                .build();

        itemRepository.save(item);

        itemRepository.deleteById(item.getId());

        Assertions.assertThatThrownBy(() -> itemRepository.findById(item.getId()).orElseGet(null)).isInstanceOf(NullPointerException.class);
    }

}