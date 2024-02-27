package com.website.repository.item.carousel;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.website.domain.item.QItemHomeCarousel.*;

@Repository
@RequiredArgsConstructor
public class ItemHomeCarouselCustomRepositoryImpl implements ItemHomeCarouselCustomRepository {

    private final JPAQueryFactory query;

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
}
