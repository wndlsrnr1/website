package com.website.repository.item;

import com.website.common.repository.item.CustomItemRepository;
import com.website.common.repository.item.CustomItemRepositoryImpl;
import com.website.common.repository.item.ItemRepository;
import com.website.config.jpa.JpaConfig;
import com.website.common.repository.attachment.AttachmentRepository;
import com.website.common.repository.category.CategoryRepository;
import com.website.common.repository.item.info.ItemInfoRepository;
import com.website.common.repository.model.attachment.Attachment;
import com.website.common.repository.model.category.Category;
import com.website.common.repository.model.category.Subcategory;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.model.item.ItemInfo;
import com.website.common.repository.subcategory.SubcategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

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