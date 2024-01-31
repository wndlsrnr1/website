package com.website.repository.item;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.web.dto.response.item.ItemDetailResponse;
import com.website.web.dto.response.item.ItemResponse;
import com.website.web.dto.response.item.QItemDetailResponse;
import com.website.web.dto.response.item.QItemResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.website.domain.attachment.QAttachment.attachment;
import static com.website.domain.category.QSubcategory.subcategory;
import static com.website.domain.item.QItem.*;
import static com.website.domain.item.QItemAttachment.*;
import static com.website.domain.item.QItemSubcategory.*;

@Repository
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
                .leftJoin(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .leftJoin(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
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
}
