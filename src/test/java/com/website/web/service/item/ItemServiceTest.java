package com.website.web.service.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.website.domain.category.Subcategory;
import com.website.repository.item.ItemRepository;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.dto.request.item.SaveItemRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.annotation.Commit;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    SubcategoryRepository subcategoryRepository;
    @Autowired
    ItemRepository itemRepository;

    //@Test
    //@Commit
    //void insertItem1000000() {
    //    Subcategory subcategory = subcategoryRepository.findAll().get(0);
    //    Long subcategoryId = subcategory.getId();
    //    for (int i = 0; i < 900000; i++) {
    //        SaveItemRequest saveItemRequest = new SaveItemRequest();
    //        saveItemRequest.setSubcategoryId(subcategoryId);
    //        int randomNumber = new Random().nextInt(10000);
    //        saveItemRequest.setNameKor("테스트" + randomNumber);
    //        saveItemRequest.setName("test" + randomNumber);
    //        saveItemRequest.setReleasedAt(LocalDateTime.now());
    //        saveItemRequest.setPrice(new Random().nextInt(10000));
    //        saveItemRequest.setQuantity(new Random().nextInt(10000));
    //        saveItemRequest.setStatus("good");
    //        saveItemRequest.setDescription("very good");
    //        itemService.saveItemByItemFormRequest(saveItemRequest, BindingResultUtils.getBindingResult(Map.of(), "name"));
    //    }
    //}

    @Test
    void test100 () {
        int size = itemRepository.findAll().size();
        log.info("size = {}", size);
    }
}