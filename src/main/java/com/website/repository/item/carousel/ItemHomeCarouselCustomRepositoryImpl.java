package com.website.repository.item.carousel;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.category.QSubcategory;
import com.website.domain.item.ItemHomeCarousel;
import com.website.domain.item.QItemHomeCarousel;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.item.carousel.CarouselSearchCond;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import com.website.web.dto.response.item.CarouselItemResponse;
import com.website.web.dto.response.item.QCarouselItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.jsse.PEMFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.website.domain.item.QItemHomeCarousel.*;
import static com.website.domain.item.QItem.*;
import static com.website.domain.attachment.QAttachment.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemHomeCarouselCustomRepositoryImpl implements ItemHomeCarouselCustomRepository {

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public void updateCarousel(CarouselUpdateRequest carouselUpdateRequests) {
        query.update(itemHomeCarousel)
                .set(itemHomeCarousel.id, carouselUpdateRequests.getId())
                .set(itemHomeCarousel.itemId, carouselUpdateRequests.getItemId())
                .set(itemHomeCarousel.attachmentId, carouselUpdateRequests.getAttachmentId())
                .set(itemHomeCarousel.priority, carouselUpdateRequests.getPriority())
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
        Integer max = 0;
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
                                itemHomeCarousel.id, itemHomeCarousel.itemId, item.name, item.nameKor, attachment.id, attachment.saveName, attachment.requestName, itemHomeCarousel.priority
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
