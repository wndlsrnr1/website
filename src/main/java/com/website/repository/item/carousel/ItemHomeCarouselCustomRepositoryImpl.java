package com.website.repository.item.carousel;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.ItemHomeCarousel;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.website.domain.item.QItemHomeCarousel.*;

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

}
