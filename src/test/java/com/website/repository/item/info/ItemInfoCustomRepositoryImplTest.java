package com.website.repository.item.info;

import com.website.domain.item.Item;
import com.website.domain.item.ItemAttachment;
import com.website.domain.item.ItemInfo;
import com.website.repository.item.ItemAttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.web.dto.request.item.info.ItemInfoEditRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
class ItemInfoCustomRepositoryImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    ItemInfoRepository itemInfoRepository;

    @Autowired
    ItemRepository itemRepository;


    @Autowired
    ItemAttachmentRepository itemAttachmentRepository;


    @Test
    void addItemInfo() {

        Item item = itemRepository.save(new Item("이름", "이름", 10, 10, "good", "good", LocalDateTime.now()));
        String brandRandom = UUID.randomUUID().toString();

        Item findItem = itemRepository.findById(item.getId()).orElse(null);
        ItemInfo itemInfo = new ItemInfo(item, 0L, 10, brandRandom, brandRandom, "china");
        ItemInfo save = itemInfoRepository.save(itemInfo);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getBrand()).isEqualTo(brandRandom);
    }

    @Test
    void editItemInfo() {
        //given
        Item item = itemRepository.save(new Item("이름", "이름", 10, 10, "good", "good", LocalDateTime.now()));
        String brandRandom = UUID.randomUUID().toString();

        ItemInfo itemInfo = new ItemInfo(item, 0L, 10, brandRandom, brandRandom, "china");
        ItemInfo save = itemInfoRepository.save(itemInfo);

        //when
        String randomAfter = UUID.randomUUID().toString();

        //editItemInfo null값 들어 갔을 때 update는 어떤 값을 넣는 지 확인하기
        itemInfoRepository.editItemInfo(new ItemInfoEditRequest(save.getId(), item.getId(), 10, randomAfter, "m", "mi"));
        ItemInfo findItemInfo = itemInfoRepository.findById(itemInfo.getId()).orElse(null);

        //then
        Assertions.assertThat(findItemInfo).isNotNull();
        Assertions.assertThat(findItemInfo.getBrand()).isEqualTo(randomAfter);
    }

    @Test
    void deleteItemInfoByItemId() {
        //given
        Item item = itemRepository.save(new Item("이름", "이름", 10, 10, "good", "good", LocalDateTime.now()));
        String brandRandom = UUID.randomUUID().toString();

        ItemInfo itemInfo = new ItemInfo(item, 0L, 10, brandRandom, brandRandom, "china");
        ItemInfo save = itemInfoRepository.save(itemInfo);

        //when
        itemInfoRepository.deleteItemInfoByItemId(item.getId());

        ItemInfo findItemInfo = itemInfoRepository.findById(save.getId()).orElse(null);

        Assertions.assertThat(findItemInfo).isNull();
    }

    @Test
    void attachmentOrder() {
        ItemAttachment itemAttachment = itemAttachmentRepository.findById(1L).orElse(null);
    }

    @Test
    public void editItemInfoV2() throws Exception {
        //given
        itemInfoRepository.updateItemInfo(null, null, null, null, 1L);

        //when

        //then

    }
}