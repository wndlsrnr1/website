package com.website.repository.item;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.controller.api.model.request.item.EditItemRequest;
import com.website.controller.api.model.request.item.EditItemRequestV2;
import com.website.controller.api.model.response.item.*;
import com.website.controller.api.model.sqlcond.item.ItemSearchCond;
import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.common.PageResult;
import com.website.repository.item.model.*;
import com.website.repository.user.model.QUser;
import com.website.repository.purchases.model.QPurchases;
import com.website.repository.review.model.QReview;
import com.website.utils.common.SearchAfterEncoder;
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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.website.repository.model.attachment.QAttachment.attachment;
import static com.website.repository.model.category.QCategory.category;
import static com.website.repository.model.category.QSubcategory.subcategory;
import static com.website.repository.model.item.QItem.item;
import static com.website.repository.model.item.QItemAttachment.itemAttachment;
import static com.website.repository.model.item.QItemInfo.itemInfo;
import static com.website.repository.model.item.QItemSubcategory.itemSubcategory;
import static com.website.repository.model.item.QItemThumbnail.itemThumbnail;
import static com.website.repository.user.model.QUser.*;
import static com.website.repository.purchases.model.QPurchases.purchases;
import static com.website.repository.review.model.QReview.*;

@Repository
@Slf4j
public class CustomItemRepositoryImpl implements CustomItemRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public CustomItemRepositoryImpl(EntityManager entityManager) {
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
    public Page<ItemResponse> getItemResponseByCondByLastItemId(
            ItemSearchCond itemSearchCond, Pageable pageable, Long lastItemId, Integer lastPageNumber, Integer pageChunk
    ) {
        if (pageChunk == null) {
            pageChunk = 5;
        }
        log.info("getItemResponseByCondByLastItemId");
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
        log.info("getItemResponseByCondWhenLastPage");
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
    public ItemBasicResponse findItemBasicResponseByItemId(Long itemId) {
        List<ItemBasicResponse> fetchResult = query
                .select(
                        new QItemBasicResponse(
                                item.id,
                                item.name,
                                item.nameKor,
                                item.price,
                                item.quantity,
                                item.status,
                                item.description,
                                item.releasedAt,
                                itemInfo.views,
                                itemInfo.salesRate,
                                itemInfo.brand,
                                itemInfo.manufacturer,
                                itemInfo.madeIn
                        )
                ).from(item)
                .join(itemInfo).on(item.id.eq(itemInfo.item.id))
                .where(item.id.eq(itemId)).fetch();

        if (fetchResult == null) {
            return null;
        }

        if (fetchResult.size() < 1) {
            return null;
        }

        return fetchResult.get(0);
    }

    @Override
    public PageResult<SearchItem> search(SearchItemCriteria criteria) {
        log.info("criteria = {}", criteria);
        List<SearchItem> result = query.select(new QSearchItem(
                        item.id,
                        item.name,
                        item.nameKor,
                        item.price,
                        item.status,
                        item.releasedAt,
                        item.createdAt,
                        category.id,
                        category.nameKor,
                        subcategory.id,
                        subcategory.nameKor,
                        itemInfo.views,
                        itemInfo.salesRate,
                        itemInfo.brand,
                        itemInfo.manufacturer,
                        itemInfo.madeIn,
                        attachment.id
                )).from(item)
                .join(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .join(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .join(category).on(subcategory.category.id.eq(category.id))
                .join(itemInfo).on(item.id.eq(itemInfo.item.id))
                .join(itemThumbnail).on(item.id.eq(itemThumbnail.item.id))
                .join(attachment).on(itemThumbnail.attachment.id.eq(attachment.id))
                .where(
                        categoryEq(criteria.getCategoryId()),
                        subcategoryEq(criteria.getSubcategoryId()),
                        whereClauseForSearch(criteria),
                        isDiscount(criteria.isOnDiscount()),
                        whereClauseForNextSearchAfter(criteria)
                )
                .orderBy(orderBySortType(criteria.getSortType()))
                .limit(criteria.getSize())
                .fetch();
        log.info("result = {}", result);

        //getNextSearchAfter
        String nextSearchAfter = getEncodedNextSearchAfter(criteria, result);

        //getTotalCount
        Long totalCount = getTotalCount(criteria);


        return PageResult.<SearchItem>builder()
                .items(result)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(totalCount)
                .build();
    }

    @Override
    public PageResult<ItemWithReview> searchItemWithReview(ItemWithReviewSearchCriteria criteria) {
        List<ItemWithReview> result = query.select(new QItemWithReview(
                        review.id,
                        item.id,
                        itemThumbnail.attachment.id,
                        item.name,
                        item.nameKor,
                        review.content,
                        user.name,
                        review.createdAt
                )).from(review)
                .join(purchases).on(review.purchases.id.eq(purchases.id))
                .join(item).on(purchases.item.id.eq(item.id))
                .join(itemThumbnail).on(item.id.eq(itemThumbnail.item.id))
                .join(user).on(purchases.user.id.eq(user.id))
                .where(
                        whereClauseForNextSearchAfter(criteria)
                )
                .orderBy(orderBySortType(criteria.getSortType()))
                .limit(criteria.getSize())
                .fetch();

        //getNextSearchAfter
        String nextSearchAfter = getEncodedNextSearchAfter(criteria, result);

        //getTotalCount
        Long totalCount = getTotalCount(criteria);


        return PageResult.<ItemWithReview>builder()
                .items(result)
                .nextSearchAfter(nextSearchAfter)
                .totalCount(totalCount)
                .build();
    }


    // ItemWithReview - Start
    private Long getTotalCount(ItemWithReviewSearchCriteria criteria) {
        if (!criteria.isWithTotalCount()) {
            return null;
        }

        return query.selectFrom(review)
                .join(purchases).on(review.purchases.id.eq(purchases.id))
                .join(item).on(purchases.item.id.eq(item.id))
                .join(itemThumbnail).on(item.id.eq(itemThumbnail.item.id))
                .join(user).on(purchases.user.id.eq(user.id))
                .fetchCount();
    }

    private String getEncodedNextSearchAfter(ItemWithReviewSearchCriteria criteria, List<ItemWithReview> result) {
        String nextSearchAfter = null;

        if (criteria.getSize() == result.size()) {
            ItemWithReview lastItem = result.get(result.size() - 1);
            switch (criteria.getSortType()) {
                case RECENT: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastItem.getReviewId().toString());
                    break;
                }
                default: {
                    throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType().name());
                }
            }
        }
        return nextSearchAfter;
    }

    private OrderSpecifier<?>[] orderBySortType(ItemWithReviewSortType sortType) {
        switch (sortType) {
            case RECENT: {
                return new OrderSpecifier[]{
                        review.id.desc(),
                };
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + sortType.name());
            }
        }
    }

    private BooleanExpression whereClauseForNextSearchAfter(ItemWithReviewSearchCriteria criteria) {
        if (criteria.getNextSearchAfter() == null) {
            return null;
        }
        switch (criteria.getSortType()) {
            case RECENT: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getNextSearchAfter());
                Long reviewId = Long.parseLong(decoded[0]);
                return review.id.lt(reviewId);
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType().name());
            }
        }
    }
    // ---------End


    private BooleanExpression whereClauseForSearch(SearchItemCriteria criteria) {
        if (criteria.getSearchName() == null) {
            return null;
        }
        return itemNameLike(criteria.getSearchName())
                .or(itemNameKorLike(criteria.getSearchName()))
                .or(subcategoryNameLike(criteria.getSearchName()))
                .or(subcategoryNameKorLike(criteria.getSearchName()))
                .or(categoryNameLike(criteria.getSearchName()))
                .or(categoryNameKorLike(criteria.getSearchName()));
    }

    private BooleanExpression isDiscount(boolean onDiscount) {
        return !onDiscount ? null : itemInfo.salesRate.gt(0);
    }

    private Long getTotalCount(SearchItemCriteria criteria) {
        if (!criteria.isWithTotalCount()) {
            return null;
        }
        return query.selectFrom(item)
                .where(
                        categoryEq(criteria.getCategoryId()),
                        subcategoryEq(criteria.getSubcategoryId()),
                        whereClauseForSearch(criteria),
                        isDiscount(criteria.isOnDiscount())
                )
                .join(itemSubcategory).on(item.id.eq(itemSubcategory.item.id))
                .join(subcategory).on(itemSubcategory.subcategory.id.eq(subcategory.id))
                .join(category).on(subcategory.category.id.eq(category.id))
                .join(itemInfo).on(item.id.eq(itemInfo.item.id))
                .join(itemThumbnail).on(item.id.eq(itemThumbnail.item.id))
                .join(attachment).on(itemThumbnail.attachment.id.eq(attachment.id))
                .fetchCount();
    }

    private String getEncodedNextSearchAfter(SearchItemCriteria criteria, List<SearchItem> result) {
        String nextSearchAfter = null;

        if (criteria.getSize() == result.size()) {
            SearchItem lastItem = result.get(result.size() - 1);
            switch (criteria.getSortType()) {
                case NAME_ASC: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastItem.getItemId().toString(), lastItem.getItemName());
                    break;
                }
                case NAME_DESC: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastItem.getItemId().toString(), lastItem.getItemName());
                    break;
                }
                case PRICE_ASC: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastItem.getItemId().toString(), lastItem.getPrice().toString());
                    break;
                }
                case PRICE_DESC: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastItem.getItemId().toString(), lastItem.getPrice().toString());
                    break;
                }
                case RECENT_CREATED: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastItem.getItemId().toString());
                    break;
                }
                case RECENT_RELEASED: {
                    nextSearchAfter = SearchAfterEncoder.encode(lastItem.getItemId().toString(), lastItem.getReleasedAt().toString());
                    break;
                }
                default: {
                    throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType().name());
                }
            }
        }
        return nextSearchAfter;
    }

    private BooleanExpression categoryNameKorLike(String searchName) {
        return searchName == null ? null : category.nameKor.like("%" + searchName + "%");
    }

    private BooleanExpression categoryNameLike(String searchName) {
        return searchName == null ? null : category.name.like("%" + searchName + "%");
    }

    private BooleanExpression subcategoryNameKorLike(String searchName) {
        return searchName == null ? null : subcategory.nameKor.like("%" + searchName + "%");
    }

    private BooleanExpression subcategoryNameLike(String searchName) {
        return searchName == null ? null : subcategory.name.like("%" + searchName + "%");
    }

    private BooleanExpression itemNameKorLike(String searchName) {
        return searchName == null ? null : item.nameKor.like("%" + searchName + "%");
    }

    private BooleanExpression itemNameLike(String searchName) {
        return searchName == null ? null : item.name.like("%" + searchName + "%");
    }

    private BooleanExpression subcategoryEq(Long subcategoryId) {
        return subcategoryId == null ? null : subcategory.id.eq(subcategoryId);
    }

    private OrderSpecifier<?>[] orderBySortType(ItemSearchSortType sortType) {
        switch (sortType) {
            case NAME_ASC: {
                return new OrderSpecifier[]{
                        item.name.asc(),
                        item.id.desc()
                };
            }
            case NAME_DESC: {
                return new OrderSpecifier[]{
                        item.name.desc(),
                        item.id.desc()
                };
            }
            case PRICE_ASC: {
                return new OrderSpecifier[]{
                        item.price.asc(),
                        item.id.desc()
                };
            }
            case PRICE_DESC: {
                return new OrderSpecifier[]{
                        item.price.desc(),
                        item.id.desc()
                };
            }
            case RECENT_CREATED: {
                return new OrderSpecifier[]{
                        item.id.desc()
                };
            }
            case RECENT_RELEASED: {
                return new OrderSpecifier[]{
                        item.releasedAt.desc(),
                        item.id.desc()
                };
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + sortType.name());
            }
        }
    }

    private BooleanExpression whereClauseForNextSearchAfter(SearchItemCriteria criteria) {
        if (criteria.getSearchAfter() == null) {
            return null;
        }
        switch (criteria.getSortType()) {
            case NAME_ASC: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getSearchAfter());
                Long itemId = Long.parseLong(decoded[0]);
                String itemName = decoded[1];
                return item.name.gt(itemName).or(
                        item.name.eq(itemName).and(item.id.lt(itemId))
                );
            }
            case NAME_DESC: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getSearchAfter());
                Long itemId = Long.parseLong(decoded[0]);
                String itemName = decoded[1];
                return item.name.lt(itemName).or(
                        item.name.eq(itemName).and(item.id.lt(itemId))
                );
            }
            case PRICE_ASC: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getSearchAfter());
                Long itemId = Long.parseLong(decoded[0]);
                Integer itemPrice = Integer.parseInt(decoded[1]);
                return item.price.gt(itemPrice).or(
                        item.price.eq(itemPrice).and(item.id.lt(itemId))
                );
            }
            case PRICE_DESC: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getSearchAfter());
                Long itemId = Long.parseLong(decoded[0]);
                Integer itemPrice = Integer.parseInt(decoded[1]);
                return item.price.lt(itemPrice).or(
                        item.price.eq(itemPrice).and(item.id.lt(itemId))
                );
            }
            case RECENT_CREATED: {
                String decoded = SearchAfterEncoder.decodeSingle(criteria.getSearchAfter());
                Long itemId = Long.parseLong(decoded);
                return item.id.lt(itemId);
            }
            case RECENT_RELEASED: {
                String[] decoded = SearchAfterEncoder.decode(criteria.getSearchAfter());
                Long itemId = Long.parseLong(decoded[0]);
                LocalDateTime itemReleasedAt = LocalDateTime.parse(decoded[1]);
                return item.releasedAt.before(itemReleasedAt).or(
                        item.releasedAt.eq(itemReleasedAt).and(item.id.lt(itemId))
                );
            }
            default: {
                throw new ClientException(ErrorCode.BAD_REQUEST, "unimplemented sort type. sorType = " + criteria.getSortType().name());
            }
        }
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
    public void updateItemByDto(Long itemId, EditItemRequestV2 editItemRequest) {

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
        if (lastItemId == null || lastPageNumber == null || pageNumber == 0 || lastItemId < 0) {
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
