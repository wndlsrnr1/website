package com.website.web.controller.api.admin.item.home.carousel;

import com.website.web.dto.request.item.carousel.CarouselAddRequest;
import com.website.web.dto.request.item.carousel.CarouselSearchCond;
import com.website.web.dto.request.item.carousel.CarouselUpdateRequest;
import com.website.web.service.item.carousel.ItemHomeCarouselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HomeCarouselManagementController {

    //private final ItemHomeCarouselRepository itemHomeCarouselRepository;
    private final ItemHomeCarouselService itemHomeCarouselService;

    @PostMapping("/home/carousel/add")
    public ResponseEntity addHomeCarousel(@Validated CarouselAddRequest carouselAddRequest, BindingResult bindingResult) {
        return itemHomeCarouselService.addCarousel(carouselAddRequest, bindingResult);
    }

    @GetMapping("/home/carousels")
    public ResponseEntity requestHomeCarouselList(@Validated CarouselSearchCond carouselSearchCond, BindingResult bindingResult, Pageable pageable) {
        return itemHomeCarouselService.getCarouselsByCond(carouselSearchCond, bindingResult, pageable);
    }

    @GetMapping("/home/carousels/{carouselId}")
    public ResponseEntity requestHomeCarouselList(@PathVariable Long carouselId) {
        return itemHomeCarouselService.getCarouselInfo(carouselId);
    }

    @PostMapping("/home/carousel/update/all")
    public ResponseEntity updateHomeCarouselPriority(@RequestBody List<CarouselUpdateRequest> carouselUpdateRequestList) {
        log.info("carouselUpdateRequestList = {}", carouselUpdateRequestList);
        return itemHomeCarouselService.updateHomeCarouselPriority(carouselUpdateRequestList);
    }

    @PostMapping(value = "/home/carousels/update/attachment/{carouselId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateHomeCarouselImage(@PathVariable Long carouselId, @RequestBody Long attachmentId) {
        //Carousel 그대로 넘어 온다.
        log.info("attachmentId = {}, carouselId = {}", attachmentId, carouselId);
        return itemHomeCarouselService.updateHomeCarouselImage(carouselId, attachmentId);
    }

    @DeleteMapping("/home/carousels/delete/{carouselId}")
    public ResponseEntity deleteHomeCarouselByCarouselId(@PathVariable Long carouselId) {
        log.info("carouselId = {}", carouselId);
        return itemHomeCarouselService.deleteCarouselById(carouselId);
    }

}
