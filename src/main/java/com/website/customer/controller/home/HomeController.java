package com.website.customer.controller.home;

import com.website.common.repository.user.CustomUserRepository;
import com.website.common.service.item.ItemService;
import com.website.common.service.item.home.ItemHomeService;
import com.website.common.service.item.carousel.ItemHomeCarouselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final CustomUserRepository customUserRepository;
    private final ItemService itemService;
    private final ItemHomeCarouselService itemHomeCarouselService;
    private final ItemHomeService itemHomeService;

    @GetMapping("/carousels")
    public ResponseEntity handleRequestForCarousel() {
        return itemHomeCarouselService.getCarousels();
    }

    @GetMapping("/main/item/recent")
    public ResponseEntity responseRequestForRecentItem() {
        return itemHomeService.getItemsResponseLatest();
    }

    @GetMapping("/main/item/special-sale")
    public ResponseEntity responseRequestForSpecialSale() {
        return itemHomeService.getItemsResponseSpecialSale();
    }

    @GetMapping("/main/item/popular")
    public ResponseEntity responseRequestForPopularItem() {
        return itemHomeService.getItemsResponsePopular();
    }


    @GetMapping("/items/list")
    public ResponseEntity responseRequestForItemList(
            String sortedBy, Pageable pageable, @RequestParam(value = "lastItemId", required = false) Long lastItemId, Integer lastPageNumber, Integer pageChunk, Boolean isLastPage, @RequestParam(value = "subcategoryId", required = false) Long subcategoryId, @RequestParam(value = "totalItems", required = false) Long totalItems
    ) {
        return itemHomeService.sendItemsResponseByCondByLastItemId(sortedBy, pageable, lastItemId, lastPageNumber, pageChunk, isLastPage, subcategoryId, totalItems);
    }

    @GetMapping("/info/item/category/subcategory/{subcategoryId}")
    public ResponseEntity sendResponseSubcategoryInfo(@PathVariable("subcategoryId") Long subcategoryId) {
        return itemHomeService.getSubcategoryInfoResponse(subcategoryId);
    }


}
