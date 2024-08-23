package com.website.repository.item;

import com.website.config.jpa.JpaConfig;
import com.website.repository.attachment.AttachmentRepository;
import com.website.repository.category.CategoryRepository;
import com.website.repository.item.info.ItemInfoRepository;
import com.website.repository.model.attachment.Attachment;
import com.website.repository.model.category.Category;
import com.website.repository.model.category.Subcategory;
import com.website.repository.model.item.Item;
import com.website.repository.model.item.ItemInfo;
import com.website.repository.subcategory.SubcategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JpaConfig.class, CustomItemRepositoryImpl.class})
@ActiveProfiles("local")
class CustomItemRepositorySearchTest {
    @Autowired
    private CustomItemRepository customItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private ItemInfoRepository itemInfoRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    private Category category1;
    private Category category2;

    private Subcategory subcategory1;
    private Subcategory subcategory2;

    private Item item1;
    private Item item2;

    private Attachment attachment1;
    private ItemInfo itemInfo1;

    @BeforeEach
    public void setUp() {



    }
}