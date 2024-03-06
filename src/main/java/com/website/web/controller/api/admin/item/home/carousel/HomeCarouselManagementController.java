package com.website.web.controller.api.admin.item.home.carousel;

import com.website.web.dto.request.item.carousel.CarouselAddRequest;
import com.website.web.dto.request.item.carousel.CarouselSearchCond;
import com.website.web.service.item.carousel.ItemHomeCarouselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
        return itemHomeCarouselService.getCarousels(carouselSearchCond, bindingResult, pageable);
    }

}
