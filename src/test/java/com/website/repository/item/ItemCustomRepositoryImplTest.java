package com.website.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.controller.api.model.request.item.home.ItemSortedByType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor
class ItemCustomRepositoryImplTest {
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

}