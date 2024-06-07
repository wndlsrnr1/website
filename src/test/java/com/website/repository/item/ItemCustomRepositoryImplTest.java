package com.website.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.Item;
import com.website.domain.item.ItemInfo;
import com.website.domain.item.QItem;
import com.website.repository.item.info.ItemInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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

    }

}