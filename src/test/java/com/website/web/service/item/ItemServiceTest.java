package com.website.web.service.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.repository.item.ItemRepository;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;

import java.util.List;
import java.util.Map;

import static com.website.domain.category.QSubcategory.subcategory;
import static com.website.domain.item.QItem.item;
import static com.website.domain.item.QItemSubcategory.itemSubcategory;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    JPAQueryFactory query;

    @Test
    void pagingByCursorLike() {
        ItemSearchCond itemSearchCond = new ItemSearchCond();
        Pageable pageable = PageRequest.of(9, 10);
        Long lastItemId = 0L;
        Integer lastPageNumber = 6;
        Integer pageChunk = 5;
        long startTime1 = System.currentTimeMillis();
        itemRepository.getItemResponseByCond(itemSearchCond, pageable);
        long endTime1 = System.currentTimeMillis();

        long startTime2 = System.currentTimeMillis();
        itemRepository.getItemResponseByCondByLastItemId(itemSearchCond, pageable, lastItemId, lastPageNumber, pageChunk);
        long endTime2 = System.currentTimeMillis();

        long beforeOptimaizedDiff = Math.abs(startTime1 - endTime1);
        long optimizedDiff = Math.abs(startTime2 - endTime2);

        log.info("beforeOptimaizedDiff = {}", beforeOptimaizedDiff);
        log.info("optimizedDiff = {}", optimizedDiff);

        Assertions.assertThat(optimizedDiff).isLessThan(beforeOptimaizedDiff);
    }

    @Test
    void countTest() {
        long startTime1 = System.currentTimeMillis();

        Long aLong = query.select(item.count())
                .from(item)
                .leftJoin(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .fetchOne();
        long endTime1 = System.currentTimeMillis();
        long beforeOptimaizedDiff = Math.abs(startTime1 - endTime1);
        log.info("beforeOptimaizedDiff = {}", beforeOptimaizedDiff);
    }


}