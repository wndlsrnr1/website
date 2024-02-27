package com.website.web.controller.api.admin.item.home.carousel;

import com.website.repository.item.carousel.ItemHomeCarouselRepository;
import com.website.web.dto.request.item.carousel.CarouselAddRequest;
import com.website.web.service.item.carousel.ItemHomeCarouselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HomeCarouselManagementController {

    private final ItemHomeCarouselRepository itemHomeCarouselRepository;
    private final ItemHomeCarouselService itemHomeCarouselService;

    @PostMapping("/admin/home/carousel/add")
    public ResponseEntity addHomeCarousel(@Validated CarouselAddRequest carouselAddRequest, BindingResult bindingResult) {
        return itemHomeCarouselService.addCarousel(carouselAddRequest, bindingResult);
    }

}
