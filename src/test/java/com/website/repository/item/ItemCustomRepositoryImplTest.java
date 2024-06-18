package com.website.repository.item;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.category.QSubcategory;
import com.website.domain.item.Item;
import com.website.domain.item.ItemInfo;
import com.website.domain.item.QItem;
import com.website.domain.item.QItemSubcategory;
import com.website.repository.item.info.ItemInfoRepository;
import com.website.web.dto.request.item.home.ItemSortedByType;
import com.website.web.dto.response.item.ItemResponse;
import com.website.web.dto.response.item.QItemResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static com.website.domain.category.QSubcategory.subcategory;
import static com.website.domain.item.QItem.*;
import static com.website.domain.item.QItemSubcategory.itemSubcategory;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

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