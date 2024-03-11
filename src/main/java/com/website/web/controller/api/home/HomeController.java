package com.website.web.controller.api.home;

import com.website.repository.user.UserRepository;
import com.website.web.dto.response.item.CarouselItemResponse;
import com.website.web.service.item.ItemService;
import com.website.web.service.item.carousel.ItemHomeCarouselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final UserRepository userRepository;
    private final ItemService itemService;
    private final ItemHomeCarouselService itemHomeCarouselService;

    @GetMapping("/carousels")
    public ResponseEntity handleRequestForCarousel() {
        return itemHomeCarouselService.getCarousels();
    }




}
