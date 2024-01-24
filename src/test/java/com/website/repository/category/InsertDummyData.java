package com.website.repository.category;

import com.website.domain.category.Category;
import com.website.domain.category.Subcategory;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.repository.user.UserRepository;
//import com.website.repository.user.UserRepository2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
public class InsertDummyData {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Test
    @Commit
    void insert() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = new Category("PLAY_STATION" + i, "플스" + i);
            Category category1 = categoryRepository.save(category);
            categories.add(category1);
            for (int j = 0; j < 10; j++) {
                Category category2 = categories.get(i);
                Subcategory subcategory = new Subcategory(category2, "PLAY_STATION" + i + "의 서브 카테고리 " + j, "플스" + i + "의 서브카테고리 " + j);
                subcategoryRepository.save(subcategory);
            }
        }
    }

    @Test
    @Commit
    void insertCategory100() {
        for (int i = 101; i < 1000; i++) {
            Category category = new Category("XBOX " + i, "엑스박스" + i);
            Category category1 = categoryRepository.save(category);
        }
    }

    @Commit
    @Test
    void insertCategory200_and_subcategory2000() {
        for (int i = 0; i < 100; i++) {
            Category category = new Category("XBOX", "엑스박스");
            categoryRepository.save(category);
            for (int j = 0; j < 100; j++) {
                Subcategory subcategory = new Subcategory(category, "game" + i + j, "게임" + i + j);
                subcategoryRepository.save(subcategory);
            }
        }

        for (int i = 0; i < 100; i++) {
            Category category = new Category("PLAYSTATION", "플레이스테이션");
            categoryRepository.save(category);
            for (int j = 0; j < 100; j++) {
                Subcategory subcategory = new Subcategory(category, "게임" + i + j, "game" + i + j);
                subcategoryRepository.save(subcategory);
            }
        }
    }

}
