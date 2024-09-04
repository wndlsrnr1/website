package com.website.common.repository.item.carousel;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.repository.item.model.QCarouselItemResponse;
import com.website.common.repository.model.item.ItemHomeCarousel;
import com.website.common.repository.item.carousel.model.CarouselSearchCond;
import com.website.common.repository.item.carousel.model.CarouselUpdateRequest;
import com.website.common.repository.item.model.CarouselItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.website.common.repository.model.attachment.QAttachment.attachment;
import static com.website.common.repository.model.item.QItem.item;
import static com.website.common.repository.model.item.QItemHomeCarousel.itemHomeCarousel;

@Repository
@Slf4j
public class ItemHomeCarouselCustomRepositoryImpl implements ItemHomeCarouselCustomRepository {

    private final JPAQueryFactory query;
    private final EntityManager em;

    public ItemHomeCarouselCustomRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public void updateCarousel(CarouselUpdateRequest carouselUpdateRequests) {
        query.update(itemHomeCarousel)
                .set(itemHomeCarousel.attachmentId, carouselUpdateRequests.getAttachmentId())
                .set(itemHomeCarousel.priority, carouselUpdateRequests.getPriority())
                .set(itemHomeCarousel.itemId, carouselUpdateRequests.getItemId())
                .where(itemHomeCarousel.id.eq(carouselUpdateRequests.getId()))
                .execute();
    }

    @Override
    public void updateCarousels(List<CarouselUpdateRequest> carouselUpdateRequestList) {

        for (CarouselUpdateRequest carouselUpdateRequest : carouselUpdateRequestList) {
            updateCarousel(carouselUpdateRequest);
        }
    }


    @Override
    public void addCarousel(Long itemId, Long imageId) {

        List<Integer> maxList = query.select(itemHomeCarousel.priority.max()).from(itemHomeCarousel).fetch();
        Integer max = 20;
        if (maxList == null || maxList.size() == 0) {
            max = 1;
            log.info("maxList = {}", maxList);
        } else {
            if (maxList.get(0) != null) {
                max = maxList.get(0) + 1;
            } else {
                max = 1;
            }
        }
        log.info("max = {}", max);
        em.persist(new ItemHomeCarousel(itemId, imageId, max));
    }

    @Override
    public Page<CarouselItemResponse> getCarouselResponseListByCond(CarouselSearchCond carouselSearchCond, Pageable pageable) {

        QueryResults<CarouselItemResponse> results = query.select(
                        new QCarouselItemResponse(
                                itemHomeCarousel.id, itemHomeCarousel.itemId, item.name, item.nameKor, attachment.id, attachment.saveName, attachment.requestName, itemHomeCarousel.priority, itemHomeCarousel.createdAt, itemHomeCarousel.updatedAt
                        )
                ).from(itemHomeCarousel)
                .leftJoin(item).on(item.id.eq(itemHomeCarousel.itemId))
                .leftJoin(attachment).on(attachment.id.eq(itemHomeCarousel.attachmentId))
                .where(
                        priceGoe(carouselSearchCond.getPriceMin()),
                        priceLoe(carouselSearchCond.getPriceMax()),
                        quantityGoe(carouselSearchCond.getQuantityMin()),
                        quantityLoe(carouselSearchCond.getQuantityMax())
                )
                .orderBy(item.nameKor.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<CarouselItemResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public CarouselItemResponse getCarouselResponseListById(Long carouselId) {
        return query.select(
                        new QCarouselItemResponse(
                                itemHomeCarousel.id, itemHomeCarousel.itemId, item.name, item.nameKor, attachment.id, attachment.saveName, attachment.requestName, itemHomeCarousel.priority, itemHomeCarousel.createdAt, itemHomeCarousel.updatedAt
                        )
                ).from(itemHomeCarousel)
                .leftJoin(item).on(item.id.eq(itemHomeCarousel.itemId))
                .leftJoin(attachment).on(attachment.id.eq(itemHomeCarousel.attachmentId))
                .where(itemHomeCarousel.id.eq(carouselId)).fetchOne();
    }

    @Override
    public void updateCarouselAttachment(Long carouselId, Long attachmentId) {
        query.update(itemHomeCarousel)
                .set(itemHomeCarousel.attachmentId, attachmentId)
                .where(itemHomeCarousel.id.eq(carouselId))
                .execute();
    }

    @Override
    public List<CarouselItemResponse> getCarouselResponseList() {

        return query.select(
                        new QCarouselItemResponse(
                                itemHomeCarousel.id, itemHomeCarousel.itemId, item.name, item.nameKor, attachment.id, attachment.saveName, attachment.requestName, itemHomeCarousel.priority, itemHomeCarousel.createdAt, itemHomeCarousel.updatedAt
                        )
                ).from(itemHomeCarousel)
                .leftJoin(item).on(item.id.eq(itemHomeCarousel.itemId))
                .leftJoin(attachment).on(attachment.id.eq(itemHomeCarousel.attachmentId))
                .orderBy(itemHomeCarousel.priority.asc())
                .fetch();
    }

    private BooleanExpression priceGoe(Integer priceMinCond) {
        return priceMinCond != null ? item.price.goe(priceMinCond) : null;
    }

    private BooleanExpression priceLoe(Integer priceMaxCond) {
        return priceMaxCond != null ? item.price.loe(priceMaxCond) : null;
    }

    private BooleanExpression quantityGoe(Integer quantityGoeCond) {
        return quantityGoeCond != null ?  item.quantity.goe(quantityGoeCond) : null;
    }

    private BooleanExpression quantityLoe(Integer quantityLoeCond) {
        return quantityLoeCond != null ? item.quantity.loe(quantityLoeCond) : null;
    }


}
