package com.website.web.controller.api.home;

import com.website.repository.user.UserRepository;
import com.website.web.dto.request.item.home.ItemHomeSearchCond;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import com.website.web.service.item.ItemService;
import com.website.web.service.item.carousel.ItemHomeCarouselService;
import com.website.web.service.item.home.ItemHomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final UserRepository userRepository;
    private final ItemService itemService;
    private final ItemHomeCarouselService itemHomeCarouselService;
    private final ItemHomeService itemHomeService;

    @GetMapping("/carousels")
    public ResponseEntity handleRequestForCarousel() {
        return itemHomeCarouselService.getCarousels();
    }

    @GetMapping("/main/item/recent")
    public ResponseEntity responseRequestForRecentItem() {
        return itemHomeService.getItemsReponseLatest();
    }

    @GetMapping("/main/item/special-sale")
    public ResponseEntity responseRequestForSpecialSale() {
        return itemHomeService.getItemsReponseSpecialSale();
    }

    @GetMapping("/main/item/popular")
    public ResponseEntity responseRequestForPopularItem() {
        return itemHomeService.getItemsResponsePopular();
    }

    @GetMapping("/item_list")
    public ResponseEntity responseRequestForItemList(
            String sortedBy, Pageable pageable, @RequestParam(value = "lastItemId", required = false) Long lastItemId, Integer lastPageNumber, Integer pageChunk, Boolean isLastPage, @RequestParam(value = "subcategoryId", required = false) Long subcategoryId
    ) {
        return itemHomeService.sendItemsResponseByCondByLastItemId(sortedBy, pageable, lastItemId, lastPageNumber, pageChunk, isLastPage, subcategoryId);
    }

}
