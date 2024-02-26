package com.website.repository.item.carousel;

import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;

import java.util.List;

public interface ItemCarouselHomeCustomRepository {

    void updateCarousel(CarouselUpdateRequest carouselUpdateRequests);


    void updateCarousels(List<CarouselUpdateRequest> carouselUpdateRequestList);
}
