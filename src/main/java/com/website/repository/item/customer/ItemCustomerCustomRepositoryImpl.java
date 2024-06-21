package com.website.repository.item.customer;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.QItem;
import com.website.web.dto.request.item.home.ItemSortedByType;
import com.website.web.dto.response.item.home.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.website.domain.category.QSubcategory.subcategory;
import static com.website.domain.item.QItem.item;
import static com.website.domain.item.QItemInfo.itemInfo;
import static com.website.domain.item.QItemSubcategory.itemSubcategory;
import static com.website.domain.item.QItemThumbnail.itemThumbnail;
import static com.website.web.dto.request.item.home.ItemSortedByType.*;

@Repository
@Slf4j
public class ItemCustomerCustomRepositoryImpl implements ItemCustomerCustomRepository{



    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public ItemCustomerCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    //React에서 선언한 값들.
    //const [subcategoryId, setSubcategoryId] = useState(null);
    //const [pageNumber, setPageNumber] = useState(0);
    //const [sortedBy, setSortedBy] = useState("name");
    //sorted 검색 값이 달라 질 경우 초기화
    //const [totalItems, setTotalItems] = useState(-1);
    //sortedBy에서 검색 값이 달라 질 경우 -1로 초기화 시켜주어야 함.
    //const [lastItemId, setLastItemId] = useState(-1);
    @Override
    public Page<ItemsForCustomerResponse> getItemsForCustomerResponseByCondByLastItemId(Long subcategoryId, String sortedBy, Long totalItems, Long lastItemId, Pageable pageable) {
        long total = getTotalForItemCustomer(subcategoryId, totalItems, lastItemId);

        List<ItemsForCustomerResponse> content = query
                .select(
                        new QItemsForCustomerResponse(
                                item.id, item.name, item.nameKor, item.price, item.quantity, item.status, item.description, item.releasedAt, item.releasedAt,
                                item.createdAt, itemSubcategory.subcategory, itemInfo.salesRate, itemInfo.views, itemThumbnail.attachment.id
                        )
                ).from(item)
                .join(itemSubcategory)
                .on(item.id.eq(itemSubcategory.item.id))
                .join(subcategory)
                .on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .join(itemInfo)
                .on(item.id.eq(itemInfo.item.id))
                .join(itemThumbnail)
                .on(item.id.eq(itemThumbnail.item.id))
                .where(
                        subcategoryEq(subcategoryId),
                        //이름이 같으면서 아이디가 더 큰 경우 + 이름 값이 더 큰 경우
                        whereClauseToFindNextStartPoint(sortedBy, lastItemId)
                )
                .orderBy(getSortedByInCustomerItems(sortedBy))
                .orderBy(item.id.asc())
                .limit(pageable.getPageSize())
                .fetch();

        //int pageNumber = getPageNumber(total, pageable.getPageSize());
        PageRequest newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return new PageImpl<>(content, newPageable, total);
    }

    @Override
    public List<ItemLatestResponse> getLatestProducts() {
        return query
                .select(new QItemLatestResponse(item.id, item.nameKor, item.releasedAt, item.price, itemInfo.salesRate, itemThumbnail.id, itemThumbnail.attachment.id))
                .from(item)
                .innerJoin(itemThumbnail)
                .on(itemThumbnail.item.id.eq(item.id))
                .innerJoin(itemInfo)
                .on(itemInfo.item.id.eq(item.id))
                .orderBy(item.releasedAt.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<ItemSpecialResponse> getSpecialSaleProducts() {
        return query
                .select(new QItemSpecialResponse(item.id, item.nameKor, item.releasedAt, item.price, itemInfo.salesRate, itemThumbnail.id, itemThumbnail.attachment.id))
                .from(item)
                .innerJoin(itemThumbnail)
                .on(itemThumbnail.item.id.eq(item.id))
                .innerJoin(itemInfo)
                .on(itemInfo.item.id.eq(item.id))
                .orderBy(itemInfo.salesRate.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<ItemPopularResponse> getPopularProducts() {
        return query
                .select(new QItemPopularResponse(item.id, item.nameKor, item.releasedAt, item.price, itemInfo.salesRate, itemThumbnail.id, itemThumbnail.attachment.id))
                .from(item)
                .innerJoin(itemThumbnail)
                .on(itemThumbnail.item.id.eq(item.id))
                .innerJoin(itemInfo)
                .on(itemInfo.item.id.eq(item.id))
                .orderBy(itemInfo.views.desc())
                .limit(10)
                .fetch();
    }

    private long getTotalForItemCustomer(Long subcategoryId, Long totalItems, Long lastItemId) {
        return lastItemId != -1 ? totalItems : (
                query
                        .select(item.id)
                        .from(item)
                        .join(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                        .join(itemInfo).on(item.id.eq(itemInfo.item.id))
                        .join(itemThumbnail).on(item.id.eq(itemThumbnail.item.id))
                        .where(
                                subcategoryEq(subcategoryId)
                        )
                        .fetchResults().getTotal()
        );
    }

    private BooleanExpression subcategoryEq(Long subcategoryId) {
        if (subcategoryId == null) {
            return null;
        }
        log.info("subcategoryId = {}", subcategoryId);
        return itemSubcategory.subcategory.id.eq(subcategoryId);
    }

    private BooleanExpression whereClauseToFindNextStartPoint(String sortedBy, Long lastItemId) {
        if (lastItemId == null || lastItemId == -1) {
            return null;
        }

        BooleanExpression valueEqualCase = valueEqualToValueButGtId(sortedBy, lastItemId);
        BooleanExpression valueGtOrLtCase = valueGtOrLtThanValue(sortedBy, lastItemId);

        if (valueEqualCase == null && valueGtOrLtCase == null) {
            return null;
        }

        if (valueEqualCase == null && valueGtOrLtCase != null) {
            return valueGtOrLtCase;
        }

        if (valueEqualCase != null && valueGtOrLtCase == null) {
            return valueEqualCase;
        }

        return valueEqualCase.or(valueGtOrLtCase);
    }

    private BooleanExpression valueEqualToValueButGtId(String sortedBy, Long lastItemId) {
        if (lastItemId == null || lastItemId == -1) {
            return null;
        }

        ItemSortedByType itemSortedByType = ItemSortedByType.fromString(sortedBy);
        QItem itemSub = new QItem("itemSub2");
        if (CREATED.equals(itemSortedByType)) {
            return item.createdAt.eq(
                            JPAExpressions
                                    .select(itemSub.createdAt)
                                    .from(itemSub)
                                    .where(itemSub.id.eq(lastItemId))
                    )
                    .and(item.id.gt(lastItemId));
        }
        if (MAX_PRICE.equals(itemSortedByType) || MIN_PRICE.equals(itemSortedByType)) {
            return item.price.eq(
                    JPAExpressions
                            .select(itemSub.price)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            ).and(item.id.gt(lastItemId));
        }

        if (NAME.equals(itemSortedByType)) {
            return item.name.eq(
                    JPAExpressions
                            .select(itemSub.name)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            ).and(item.id.gt(lastItemId));
        }

        if (RELEASED.equals(itemSortedByType)) {
            return item.releasedAt.eq(
                    JPAExpressions
                            .select(itemSub.releasedAt)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            ).and(item.id.gt(lastItemId));
        }

        return null;
    }

    private BooleanExpression valueGtOrLtThanValue(String sortedBy, Long lastItemId) {
        log.info("sortedBy = {}", sortedBy);
        log.info("lastItemId = {}", lastItemId);

        if (lastItemId == null || lastItemId == -1) {
            return null;
        }

        ItemSortedByType itemSortedByType = ItemSortedByType.fromString(sortedBy);
        QItem itemSub = new QItem("itemSub");
        if (CREATED.equals(itemSortedByType)) {
            return item.createdAt.lt(
                    JPAExpressions
                            .select(itemSub.createdAt)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            );
        }
        if (MAX_PRICE.equals(itemSortedByType)) {
            return item.price.lt(
                    JPAExpressions
                            .select(itemSub.price)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            );
        }

        if (MIN_PRICE.equals(itemSortedByType)) {
            return item.price.gt(
                    JPAExpressions
                            .select(itemSub.price)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            );
        }

        if (NAME.equals(itemSortedByType)) {
            return item.name.gt(
                    JPAExpressions
                            .select(itemSub.name)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            );
        }

        if (RELEASED.equals(itemSortedByType)) {
            return item.releasedAt.gt(
                    JPAExpressions
                            .select(itemSub.releasedAt)
                            .from(itemSub)
                            .where(itemSub.id.eq(lastItemId))
            );
        }

        return null;
    }

    private OrderSpecifier<?> getSortedByInCustomerItems(String sortedByString) {
        if (sortedByString == null) {
            return null;
        }
        switch (ItemSortedByType.fromString(sortedByString)) {
            case MAX_PRICE:
                return item.price.desc();
            case MIN_PRICE:
                return item.price.asc();
            case NAME:
                return item.nameKor.asc();
            case RELEASED:
                return item.releasedAt.desc();
            case CREATED:
                return item.createdAt.desc();
            default:
                return item.id.asc();
        }
    }
}
