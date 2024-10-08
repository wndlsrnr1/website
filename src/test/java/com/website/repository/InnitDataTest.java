package com.website.repository;

import com.website.common.repository.model.category.Category;
import com.website.common.repository.model.category.Subcategory;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.model.item.ItemSubcategory;
import com.website.common.repository.category.CategoryRepository;
import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.itemsubcategory.ItemSubcategoryRepository;
import com.website.common.repository.subcategory.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
public class InnitDataTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    SubcategoryRepository subcategoryRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemSubcategoryRepository itemSubcategoryRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    //@Commit
    void item_innit() {
        String[] arr = {"XBOX", "PLAYSTATION"};
        String[] arr2 = {"엑스박스", "플레이스테이션"};
        for (int i = 0; i < 10; i++) {
            Category category = new Category(arr[i % 2], arr2[i % 2]);
            categoryRepository.save(category);
            for (int j = 0; j < 10; j++) {
                Subcategory subcategory = new Subcategory(category, "genre" + (i * 10) + j, "장르" + (i * 10) + j);
                //Item item = new Item("game" + i, "게임" + i, i * 1000, i * 10000, "good", "description", LocalDateTime.now());
                Item item = itemRepository.save(
                        Item.builder()
                                .name("game" + i)
                                .nameKor("게임" + i)
                                .price(i * 1000)
                                .quantity(i * 10000)
                                .status("good")
                                .description("description")
                                .releasedAt(LocalDateTime.now())
                                .build()
                );
                itemRepository.save(item);
                subcategoryRepository.save(subcategory);
                itemSubcategoryRepository.save(new ItemSubcategory(item, subcategory));
            }
        }
    }

    @Test
    //@Commit
    void testConnection() {
        String[] arr = {"XBOX", "PLAYSTATION"};
        String[] arr2 = {"엑스박스", "플레이스테이션"};
        for (int i = 0; i < 10; i++) {
            Category category = new Category(arr[i % 2], arr2[i % 2]);
            categoryRepository.save(category);
            for (int j = 0; j < 10; j++) {
                Subcategory subcategory = new Subcategory(category, "genre" + (i * 10) + j, "장르" + (i * 10) + j);
                //Item item = new Item("game" + i, "게임" + i, i * 1000, i * 10000, "good", "description", LocalDateTime.now());
                Item item = itemRepository.save(
                        Item.builder()
                                .name("game" + i)
                                .nameKor("게임" + i)
                                .price(i * 1000)
                                .quantity(i * 10000)
                                .status("good")
                                .description("description")
                                .releasedAt(LocalDateTime.now())
                                .build()
                );
                itemRepository.save(item);
                subcategoryRepository.save(subcategory);
                itemSubcategoryRepository.save(new ItemSubcategory(item, subcategory));
            }
        }
    }
}
