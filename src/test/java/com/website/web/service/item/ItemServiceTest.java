package com.website.web.service.item;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.repository.item.ItemRepository;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.dto.response.item.QItemResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import static com.website.domain.category.QSubcategory.subcategory;
import static com.website.domain.item.QItem.item;
import static com.website.domain.item.QItemSubcategory.itemSubcategory;

@Slf4j
@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    JPAQueryFactory query;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    ItemRepository itemRepository;

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
        boolean isLastPage = false;
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

    @Test
    void timeCheck() {
        ItemSearchCond itemSearchCond = new ItemSearchCond();
        itemSearchCond.setCategoryId(2L);
        Long timeDiff = measureTime(() -> query.select(
                        new QItemResponse(
                                item.id,
                                item.name,
                                item.nameKor,
                                itemSubcategory.subcategory
                        )
                )
                .from(item)
                .leftJoin(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .leftJoin(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .where(
                        categoryEq(itemSearchCond.getCategoryId())
                )
                .offset(200000)
                .limit(100)
                .fetch());

        Long timeDiff2 = measureTime(() -> query.select(
                        new QItemResponse(
                                item.id,
                                item.name,
                                item.nameKor,
                                itemSubcategory.subcategory
                        )
                )
                .from(item)
                .innerJoin(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .innerJoin(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .where(
                        categoryEq(itemSearchCond.getCategoryId())
                )
                .offset(200000)
                .limit(100)
                .fetch());

        log.info("timeDiff1 = {}", timeDiff);
        log.info("timeDiff2 = {}", timeDiff2);
        Assertions.assertThat(timeDiff2).isLessThan(timeDiff);
    }

    //
    //@Test
    //@Commit
    //void insertItem1000000() {
    //    Subcategory subcategory = subcategoryRepository.findAll().get(0);
    //    Long subcategoryId = subcategory.getId();
    //    for (int i = 0; i < 1000000; i++) {
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
    //        itemService.saveItemByItemFormRequest(saveItemRequest, BindingResultUtils.getBindingResult(Map.of("name", "name"), "name"));
    //    }
    //}

    public static Long measureTime(Runnable task) {
        long before = System.currentTimeMillis();
        task.run();
        long after = System.currentTimeMillis();
        return Math.abs(before - after);
    }

    private BooleanExpression nameOrNameKorLike(String searchName) {
        return searchName != null && StringUtils.hasText(searchName) ? item.name.contains(searchName).or(item.nameKor.contains(searchName)) : null;
    }

    private BooleanExpression priceGoe(Integer priceMinCond) {
        return priceMinCond != null ? item.price.goe(priceMinCond) : null;
    }

    private BooleanExpression priceLoe(Integer priceMaxCond) {
        return priceMaxCond != null ? item.price.loe(priceMaxCond) : null;
    }

    private BooleanExpression quantityGoe(Integer quantityMinCond) {
        return quantityMinCond != null ? item.quantity.goe(quantityMinCond) : null;
    }

    private BooleanExpression quantityLoe(Integer quantityMaxCond) {
        return quantityMaxCond != null ? item.quantity.loe(quantityMaxCond) : null;
    }

    private BooleanExpression searchNameEq(String searchName) {
        return StringUtils.hasText(searchName) ? item.name.eq(searchName) : null;
    }

    private BooleanExpression categoryEq(Long categoryIdCond) {
        return (categoryIdCond != null && categoryIdCond != -1) ? subcategory.category.id.eq(categoryIdCond) : null;
    }

    private BooleanExpression itemIdGtOrLt(Long lastItemId, Integer lastPageNumber, Integer pageNumber) {
        if (lastItemId == null || lastPageNumber == null) {
            return null;
        }
        if (lastPageNumber <= pageNumber) {
            return item.id.loe(lastItemId);
        }
        return item.id.gt(lastItemId);
    }

    private OrderSpecifier<Long> getOrder(Integer lastPageNumber, Integer pageNumber) {
        if (lastPageNumber == null || pageNumber == null) {
            return item.id.asc();
        }

        if (lastPageNumber >= pageNumber) {
            return item.id.desc();
        }
        return item.id.asc();
    }

    private Long getOffSet(Pageable pageable, Integer lastPageNumber) {
        if (lastPageNumber == null) {
            return 0L;
        }

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        return (long) Math.abs(pageNumber - lastPageNumber) * pageSize;
    }
}