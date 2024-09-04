package com.website.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.controller.item.model.home.ItemSortedByType;
import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.model.item.Item;
import com.website.repository.model.item.QItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor
class CustomItemRepositoryImplTest {
    @Autowired
    EntityManager entityManager;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private JPAQueryFactory query;


    @Test
    @Transactional
    void querydslItemUpdateTest() {
        ItemSortedByType itemSortedByType = ItemSortedByType.valueOf("NAME");
        System.out.println("itemSortedByType = " + itemSortedByType);
    }

    @Test
    void whereTest() {
        Item item = Item.builder()
                .nameKor("asdf")
                .name("asdf").build();
        itemRepository.save(item);
        List<Item> fetch = query.select(QItem.item).from(QItem.item)
                .where(QItem.item.name.eq("asdf"))
                .where(QItem.item.name.eq("asdf"))
                .fetch();

        System.out.println("fetch = " + fetch);
    }
}