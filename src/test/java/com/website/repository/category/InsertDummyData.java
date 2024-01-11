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





}
