package com.website.web.controller.api.home;

import com.website.repository.user.UserRepository;
import com.website.web.dto.response.item.CarouselItemResponse;
import com.website.web.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final ItemService itemService;

    @GetMapping("/carousel/items")
    public ResponseEntity<List<CarouselItemResponse>> handleRequestForCarousel() {
        return itemService.getCarouselItemsInHome();
    }


}
