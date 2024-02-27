package com.website.web.service.item.carousel;

import com.website.domain.item.ItemHomeCarousel;
import com.website.repository.item.carousel.ItemHomeCarouselRepository;
import com.website.web.dto.request.item.carousel.CarouselAddRequest;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
/**
 * HomeCarousel 작업 하는 도중 이었음. ResponseEntity형태로 return 하려면 서비스 하나하나 처리 하기
 */
public class ItemHomeCarouselService {

    private final ItemHomeCarouselRepository itemHomeCarouselRepository;

    @Transactional
    public ResponseEntity addCarousel(CarouselAddRequest carouselAddRequest, BindingResult bindingResult) {
        ItemHomeCarousel itemHomeCarousel = getItemCarouselHome(carouselAddRequest);
        itemHomeCarouselRepository.save(itemHomeCarousel);
        return null;
    }

    @Transactional
    public void addCarousels(List<CarouselAddRequest> carouselAddRequests) {
        List<ItemHomeCarousel> itemHomeCarouselList = new ArrayList<>();

        for (CarouselAddRequest carouselAddRequest : carouselAddRequests) {
            ItemHomeCarousel itemHomeCarousel = getItemCarouselHome(carouselAddRequest);
            itemHomeCarouselList.add(itemHomeCarousel);
        }

        itemHomeCarouselRepository.saveAll(itemHomeCarouselList);
    }

    @Transactional
    public void deleteCarousel(Long carouselId) {
        itemHomeCarouselRepository.deleteById(carouselId);
    }

    @Transactional
    public void deleteCarousels(List<Long> carouselIds) {
        itemHomeCarouselRepository.deleteAllById(carouselIds);
    }

    @Transactional
    public void updateCarousel(CarouselUpdateRequest carouselUpdateRequest) {
        itemHomeCarouselRepository.updateCarousel(carouselUpdateRequest);
    }

    @Transactional
    public void updateCarousels(List<CarouselUpdateRequest> carouselUpdateRequestList) {
        itemHomeCarouselRepository.updateCarousels(carouselUpdateRequestList);
    }

    private ItemHomeCarousel getItemCarouselHome(CarouselAddRequest carouselAddRequest) {
        Long itemId = carouselAddRequest.getItemId();
        Long attachmentId = carouselAddRequest.getAttachmentId();
        Integer priority = carouselAddRequest.getPriority();
        ItemHomeCarousel itemHomeCarousel = new ItemHomeCarousel(itemId, attachmentId, priority);
        return itemHomeCarousel;
    }
}
