package com.website.repository;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.domain.item.Item;
import com.website.domain.item.ItemSubcategory;
import com.website.repository.category.CategoryRepository;
import com.website.repository.item.ItemRepository;
import com.website.repository.itemsubcategory.ItemSubcategoryRepository;
import com.website.repository.subcategory.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

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
    @Commit
    void item_innit() {
        String[] arr = {"XBOX", "PLAYSTATION"};
        String[] arr2 = {"엑스박스", "플레이스테이션"};
        for (int i = 0; i < 10; i++) {
            Category category = new Category(arr[i % 2], arr2[i % 2]);
            categoryRepository.save(category);
            for (int j = 0; j < 10; j++) {
                Subcategory subcategory = new Subcategory(category, "genre" + (i * 10) + j, "장르" + (i * 10) + j);
                Item item = new Item("game" + i, "게임" + i, i * 1000, i * 10000, "good", "description", LocalDateTime.now());
                itemRepository.save(item);
                subcategoryRepository.save(subcategory);
                itemSubcategoryRepository.save(new ItemSubcategory(item, subcategory));
            }
        }
    }
}
