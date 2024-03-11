package com.website.web.service.item.carousel;

import com.website.domain.attachment.Attachment;
import com.website.domain.item.Item;
import com.website.domain.item.ItemAttachment;
import com.website.domain.item.ItemHomeCarousel;
import com.website.repository.attachment.AttachmentRepository;
import com.website.repository.item.ItemAttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.repository.item.carousel.ItemHomeCarouselRepository;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.item.carousel.CarouselAddRequest;
import com.website.web.dto.request.item.carousel.CarouselPriorityUpdateRequest;
import com.website.web.dto.request.item.carousel.CarouselSearchCond;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import com.website.web.dto.response.item.CarouselItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ItemAttachmentRepository itemAttachmentRepository;
    private final MessageSource messageSource;
    private final ItemRepository itemRepository;
    private final AttachmentRepository attachmentRepository;
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

    private ItemHomeCarousel getItemCarouselHome(CarouselAddRequest carouselAddRequest) {
        Long itemId = carouselAddRequest.getItemId();
        Long attachmentId = carouselAddRequest.getAttachmentId();
        Integer priority = carouselAddRequest.getPriority();
        ItemHomeCarousel itemHomeCarousel = new ItemHomeCarousel(itemId, attachmentId, priority);
        return itemHomeCarousel;
    }

    public ResponseEntity getCarousels(CarouselSearchCond carouselSearchCond, BindingResult bindingResult, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            ApiResponseBody body = ApiResponseBody.builder()
                    .apiError(new ApiError(bindingResult))
                    .data(null)
                    .message("binding error")
                    .build();
            return ResponseEntity.badRequest().body(body);
        }

        //서비스 에러

        //정상 흐름

        Page<CarouselItemResponse> carouselResponseList = itemHomeCarouselRepository.getCarouselResponseListByCond(carouselSearchCond, pageable);
        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(carouselResponseList)
                .apiError(null)
                .message("ok").build();

        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity updateHomeCarouselPriority(List<CarouselUpdateRequest> carouselUpdateRequestList) {
        log.info("carouselUpdateRequestList = {}", carouselUpdateRequestList);

        //바인딩 에러 => json형식에서 바인딩 에러 처리하는 법 찾기


        //DB inconsistency 에러 => 일반적으로 처리하는 로직 찾기 => 주말에
        for (CarouselUpdateRequest carouselUpdateRequest : carouselUpdateRequestList) {
            Long itemId = carouselUpdateRequest.getItemId();
            Long attachmentId = carouselUpdateRequest.getAttachmentId();
            if (itemId == null || attachmentId == null) {
                return ResponseEntity.badRequest().build();
            }

            Item item = itemRepository.findById(itemId).orElse(null);
            if (item == null) {
                return ResponseEntity.badRequest().build();
            }

            Attachment attachment = attachmentRepository.findById(attachmentId).orElse(null);
            if (attachment == null) {
                return ResponseEntity.badRequest().build();
            }
        }

        //정상흐름
        itemHomeCarouselRepository.updateCarousels(carouselUpdateRequestList);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity getCarouselInfo(Long carouselId) {
        if (carouselId == null) {
            return ResponseEntity.badRequest().build();
        }

        CarouselItemResponse carouselResponseListById = itemHomeCarouselRepository.getCarouselResponseListById(carouselId);

        if (carouselResponseListById == null) {
            return ResponseEntity.badRequest().build();
        }

        ApiResponseBody<Object> body = ApiResponseBody.builder().data(carouselResponseListById).build();
        return ResponseEntity.ok(body);
    }
}
