package com.website.web.service.item.carousel;

import com.website.domain.item.ItemCarouselHome;
import com.website.repository.item.carousel.ItemCarouselHomeRepository;
import com.website.web.dto.request.item.carousel.CarouselAddRequest;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
/**
 * HomeCarousel 작업 하는 도중 이었음. ResponseEntity형태로 return 하려면 서비스 하나하나 처리 하기
 */
public class ItemCarouselHomeService {

    private final ItemCarouselHomeRepository itemCarouselHomeRepository;

    @Transactional
    public void addCarousel(CarouselAddRequest carouselAddRequest) {
        ItemCarouselHome itemCarouselHome = getItemCarouselHome(carouselAddRequest);
        itemCarouselHomeRepository.save(itemCarouselHome);
    }

    @Transactional
    public void addCarousels(List<CarouselAddRequest> carouselAddRequests) {
        List<ItemCarouselHome> itemCarouselHomeList = new ArrayList<>();

        for (CarouselAddRequest carouselAddRequest : carouselAddRequests) {
            ItemCarouselHome itemCarouselHome = getItemCarouselHome(carouselAddRequest);
            itemCarouselHomeList.add(itemCarouselHome);
        }

        itemCarouselHomeRepository.saveAll(itemCarouselHomeList);
    }

    @Transactional
    public void deleteCarousel(Long carouselId) {
        itemCarouselHomeRepository.deleteById(carouselId);
    }

    @Transactional
    public void deleteCarousels(List<Long> carouselIds) {
        itemCarouselHomeRepository.deleteAllById(carouselIds);
    }

    @Transactional
    public void updateCarousel(CarouselUpdateRequest carouselUpdateRequest) {
        itemCarouselHomeRepository.updateCarousel(carouselUpdateRequest);
    }

    @Transactional
    public void updateCarousels(List<CarouselUpdateRequest> carouselUpdateRequestList) {
        itemCarouselHomeRepository.updateCarousels(carouselUpdateRequestList);
    }

    private ItemCarouselHome getItemCarouselHome(CarouselAddRequest carouselAddRequest) {
        Long itemId = carouselAddRequest.getItemId();
        Long attachmentId = carouselAddRequest.getAttachmentId();
        Integer priority = carouselAddRequest.getPriority();
        ItemCarouselHome itemCarouselHome = new ItemCarouselHome(itemId, attachmentId, priority);
        return itemCarouselHome;
    }
}
