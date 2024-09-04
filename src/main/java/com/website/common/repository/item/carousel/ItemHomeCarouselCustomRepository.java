package com.website.common.repository.item.carousel;

import com.website.common.repository.item.carousel.model.CarouselSearchCond;
import com.website.common.repository.item.carousel.model.CarouselUpdateRequest;
import com.website.common.repository.item.model.CarouselItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemHomeCarouselCustomRepository {

    void updateCarousel(CarouselUpdateRequest carouselUpdateRequests);

    void updateCarousels(List<CarouselUpdateRequest> carouselUpdateRequestList);

    void addCarousel(Long itemId, Long imageId);

    Page<CarouselItemResponse> getCarouselResponseListByCond(CarouselSearchCond carouselSearchCond, Pageable pageable);

    CarouselItemResponse getCarouselResponseListById(Long carouselId);

    void updateCarouselAttachment(Long carouselId, Long attachmentId);

    List<CarouselItemResponse> getCarouselResponseList();

}
