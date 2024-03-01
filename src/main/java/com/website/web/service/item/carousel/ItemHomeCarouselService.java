package com.website.web.service.item.carousel;

import com.website.domain.item.Item;
import com.website.domain.item.ItemAttachment;
import com.website.domain.item.ItemHomeCarousel;
import com.website.repository.item.ItemAttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.repository.item.carousel.ItemHomeCarouselRepository;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.item.carousel.CarouselAddRequest;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
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
    private final ItemRepository itemRepository;
    private final ItemAttachmentRepository itemAttachmentRepository;
    private final MessageSource messageSource;

    @Transactional
    public ResponseEntity addCarousel(CarouselAddRequest carouselAddRequest, BindingResult bindingResult) {
        //binding Error
        if (bindingResult.hasErrors()) {
            ApiError apiError = new ApiError(bindingResult);
            ApiResponseBody<Object> body = ApiResponseBody.builder()
                    .apiError(apiError)
                    .message("binding Error")
                    .build();
            return ResponseEntity.badRequest().body(body);
        }

        //정합성 에러
        Long itemId = carouselAddRequest.getItemId();
        Long attachmentId = carouselAddRequest.getAttachmentId();
        ItemAttachment itemAttachment = itemAttachmentRepository.findByItemIdAndAttachmentId(itemId, attachmentId);
        if (itemAttachment == null) {
            String message = messageSource.getMessage("Nodata.itemId", null, null);
            String field = "itemId";
            ApiError apiError = new ApiError(field, message);
            ApiResponseBody<Object> body = ApiResponseBody.builder()
                    .apiError(apiError)
                    .build();
            return ResponseEntity.badRequest().body(body);
        }

        //Ok
        ItemHomeCarousel itemHomeCarousel = getItemCarouselHome(carouselAddRequest);
        itemHomeCarouselRepository.addCarousel(itemHomeCarousel.getItemId(), itemHomeCarousel.getAttachmentId());
        return ResponseEntity.ok().build();
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
