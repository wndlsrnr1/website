package com.website.repository.item.carousel;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.website.domain.item.QItemCarouselHome.*;

@Repository
@RequiredArgsConstructor
public class ItemCarouselHomeCustomRepositoryImpl implements ItemCarouselHomeCustomRepository{

    private final JPAQueryFactory query;

    @Override
    public void updateCarousel(CarouselUpdateRequest carouselUpdateRequests) {
        query.update(itemCarouselHome)
                .set(itemCarouselHome.id, carouselUpdateRequests.getId())
                .set(itemCarouselHome.itemId, carouselUpdateRequests.getItemId())
                .set(itemCarouselHome.attachmentId, carouselUpdateRequests.getAttachmentId())
                .set(itemCarouselHome.priority, carouselUpdateRequests.getPriority())
                .execute();
    }

    @Override
    public void updateCarousels(List<CarouselUpdateRequest> carouselUpdateRequestList) {
        for (CarouselUpdateRequest carouselUpdateRequest : carouselUpdateRequestList) {
            updateCarousel(carouselUpdateRequest);
        }
    }
}
