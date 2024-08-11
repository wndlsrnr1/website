package com.website.repository.item.carousel;

import com.website.controller.api.model.request.item.carousel.CarouselSearchCond;
import com.website.controller.api.model.request.item.carousel.CarouselUpdateRequest;
import com.website.controller.api.model.response.item.CarouselItemResponse;
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
