package com.website.repository.item;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.Item;
import com.website.domain.item.QItemInfo;
import com.website.domain.item.QItemThumbnail;
import com.website.web.dto.request.item.EditItemRequest;
import com.website.web.dto.response.item.*;
import com.website.web.dto.response.item.home.*;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityManager;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.website.domain.attachment.QAttachment.attachment;
import static com.website.domain.category.QSubcategory.subcategory;
import static com.website.domain.item.QItem.*;
import static com.website.domain.item.QItemAttachment.*;
import static com.website.domain.item.QItemInfo.itemInfo;
import static com.website.domain.item.QItemSubcategory.*;
import static com.website.domain.item.QItemThumbnail.itemThumbnail;

@Repository
@Slf4j
public class ItemCustomRepositoryImpl implements ItemCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public ItemCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
        //this.entityUpdater = new EntityUpdater(entityManager);
    }


    @Override
    public void updateNameById(String updateName, Long id) {
        query.update(item)
                .set(item.name, updateName)
                .where(item.id.eq(id));
    }

    @Override
    public Page<ItemResponse> getItemResponseByCondByLastItemId(ItemSearchCond itemSearchCond, Pageable pageable, Long lastItemId, Integer lastPageNumber, Integer pageChunk) {
        if (pageChunk == null) {
            pageChunk = 5;
        }

        List<ItemResponse> content = query.select(
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
                        nameOrNameKorLike(itemSearchCond.getSearchName()),
                        priceGoe(itemSearchCond.getPriceMin()),
                        priceLoe(itemSearchCond.getPriceMax()),
                        quantityGoe(itemSearchCond.getQuantityMin()),
                        quantityLoe(itemSearchCond.getQuantityMax()),
                        itemIdGtOrLt(lastItemId, lastPageNumber, pageable.getPageNumber()),
                        categoryEq(itemSearchCond.getCategoryId())
                )
                .orderBy(getOrder(lastPageNumber, pageable.getPageNumber()))
                .offset(getOffSet(pageable, lastPageNumber))
                .limit(pageable.getPageSize())
                .fetch()
                .stream().sorted(Comparator.comparingLong(ItemResponse::getId)).collect(Collectors.toList());

        Long total = getTotal(itemSearchCond, pageable.getPageNumber(), pageChunk, content, pageable);

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ItemResponse> getItemResponseByCondWhenLastPage(ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable, Long lastItemId, Integer lastPageNumber, Integer pageChunk, Boolean isLastPage) {
        int pageSize = pageable.getPageSize();

        QueryResults<ItemResponse> result = query.select(
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
                        nameOrNameKorLike(itemSearchCond.getSearchName()),
                        priceGoe(itemSearchCond.getPriceMin()),
                        priceLoe(itemSearchCond.getPriceMax()),
                        quantityGoe(itemSearchCond.getQuantityMin()),
                        quantityLoe(itemSearchCond.getQuantityMax()),
                        categoryEq(itemSearchCond.getCategoryId())
                ).orderBy(item.id.desc()).limit(1).fetchResults();

        long total = result.getTotal();
        List<ItemResponse> results = result.getResults();
        ItemResponse itemResponse = results.stream().findAny().orElse(null);

        long limit = getLimit(pageSize, total);

        List<ItemResponse> content = query.select(
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
                        nameOrNameKorLike(itemSearchCond.getSearchName()),
                        priceGoe(itemSearchCond.getPriceMin()),
                        priceLoe(itemSearchCond.getPriceMax()),
                        quantityGoe(itemSearchCond.getQuantityMin()),
                        quantityLoe(itemSearchCond.getQuantityMax()),
                        categoryEq(itemSearchCond.getCategoryId()),
                        item.id.loe(itemResponse.getId())
                )
                .orderBy(item.id.desc())
                .limit(limit)
                .fetch().stream().sorted(Comparator.comparingLong(ItemResponse::getId)).collect(Collectors.toList());

        int pageNumber = getPageNumber(total, pageSize);
        PageRequest obj = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(content, obj, total);
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

    private long getLimit(int pageSize, long total) {
        long limit = (total % pageSize);
        if (total != 0 && limit == 0) {
            limit = pageSize;
        }
        return limit;
    }

    private int getPageNumber(long total, int pageSize) {
        if (total == 0) {
            return 0;
        }

        return (int) ((total - 1) / pageSize);
    }

    private Long getTotal(ItemSearchCond itemSearchCond, int pageNumber, Integer pageChunk, List<ItemResponse> content, Pageable pageable) {
        if (content.size() < pageable.getPageSize()) {
            return pageNumber * (long) pageChunk + content.size();
        }

        ItemResponse lastItem = content.get(content.size() - 1);
        Long lastId = lastItem.getId();
        long forNow = ((long) pageNumber + 1) * pageable.getPageSize();
        Integer limit = (pageChunk * ((pageNumber / pageChunk) + 1) - pageNumber - 1) * pageable.getPageSize() + 1;
        log.info("lastId = {}, forNow={}, limit = {}", lastId, forNow, limit);

        Integer additional = query.select(item)
                .from(
                        item
                )
                .innerJoin(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .innerJoin(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .where(
                        nameOrNameKorLike(itemSearchCond.getSearchName()),
                        priceGoe(itemSearchCond.getPriceMin()),
                        priceLoe(itemSearchCond.getPriceMax()),
                        quantityGoe(itemSearchCond.getQuantityMin()),
                        quantityLoe(itemSearchCond.getQuantityMax()),
                        categoryEq(itemSearchCond.getCategoryId()),
                        item.id.gt(lastId)
                )
                .limit(limit)
                .fetch().size();
        return forNow + additional;
    }


    @Override
    public Page<ItemResponse> getItemResponseByCond(ItemSearchCond itemSearchCond, Pageable pageable) {
        QueryResults<ItemResponse> results = query.select(
                        new QItemResponse(
                                item.id,
                                item.name,
                                item.nameKor,
                                item.price,
                                item.quantity,
                                item.status,
                                item.description,
                                item.releasedAt,
                                item.updatedAt,
                                item.createdAt,
                                subcategory
                        )
                )
                .from(item)
                .innerJoin(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .innerJoin(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .where(
                        nameOrNameKorLike(itemSearchCond.getSearchName()),
                        priceGoe(itemSearchCond.getPriceMin()),
                        priceLoe(itemSearchCond.getPriceMax()),
                        quantityGoe(itemSearchCond.getQuantityMin()),
                        quantityLoe(itemSearchCond.getQuantityMax()),
                        categoryEq(itemSearchCond.getCategoryId())
                )
                .orderBy(item.nameKor.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ItemResponse> content = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<ItemDetailResponse> findItemDetailResponse(Long itemId) {
        List<ItemDetailResponse> itemDetailResponse = query.select(
                        new QItemDetailResponse(
                                item.id,
                                item.name,
                                item.nameKor,
                                item.price,
                                item.quantity,
                                item.status,
                                item.description,
                                item.releasedAt,
                                item.updatedAt,
                                item.createdAt,
                                subcategory,
                                attachment.id,
                                attachment.saveName,
                                attachment.requestName
                        )
                )
                .from(item)
                .leftJoin(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .leftJoin(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .leftJoin(itemAttachment).on(item.id.eq(itemAttachment.item.id))
                .leftJoin(attachment).on(itemAttachment.attachment.id.eq(attachment.id))
                .where(
                        item.id.eq(itemId)
                )
                .fetch();
        return itemDetailResponse;
    }

    @Override
    public void deleteFileOnItem(List<Long> fileIdList) {
        for (Long fileId : fileIdList) {
            query.delete(itemAttachment).where(itemAttachment.attachment.id.eq(fileId)).execute();
            query.delete(attachment).where(attachment.id.eq(fileId)).execute();
        }
    }

    @Override
    public void updateItemByDto(Long itemId, EditItemRequest editItemRequest) {
        /*
    private Long subcategoryId;
    private List<MultipartFile> imageFiles;
    private List<String> images;
         */
        query.update(item)
                .set(item.name, item.name)
                .set(item.nameKor, editItemRequest.getNameKor())
                .set(item.status, editItemRequest.getStatus())
                .set(item.description, editItemRequest.getDescription())
                .set(item.quantity, editItemRequest.getQuantity())
                .set(item.price, editItemRequest.getPrice())
                .set(item.releasedAt, editItemRequest.getReleasedAt())
                .where(item.id.eq(itemId))
                .execute();
    }

    @Override
    public ResponseEntity<List<CarouselItemResponse>> getCarouselItemsInHome() {
        return null;
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
        if (lastItemId == null || lastPageNumber == null || pageNumber == 0) {
            return null;
        }
        if (lastPageNumber < pageNumber) {
            return item.id.gt(lastItemId);
        }
        return item.id.loe(lastItemId);
    }

    private OrderSpecifier<Long> getOrder(Integer lastPageNumber, Integer pageNumber) {
        if (lastPageNumber == null || pageNumber == null || pageNumber == 0) {
            return item.id.asc();
        }
        if (lastPageNumber >= pageNumber) {
            return item.id.desc();
        }
        return item.id.asc();
    }

    private Long getOffSet(Pageable pageable, Integer lastPageNumber) {
        if (lastPageNumber == null || pageable.getPageNumber() == 0) {
            return 0L;
        }
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        if (pageable.getPageNumber() <= lastPageNumber) {
            return ((long) lastPageNumber - pageable.getPageNumber()) * pageSize;
        }
        return (long) Math.abs(pageNumber - lastPageNumber - 1) * pageSize;
    }

}
