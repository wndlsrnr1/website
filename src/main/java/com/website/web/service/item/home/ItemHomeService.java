package com.website.web.service.item.home;

import com.website.repository.item.ItemRepository;
import com.website.web.dto.response.item.home.ItemLatestResponse;
import com.website.web.dto.response.item.home.ItemPopularResponse;
import com.website.web.dto.response.item.home.ItemSpecialResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemHomeService {

    private final ItemRepository itemRepository;

    public ResponseEntity getItemsReponseLatest() {
        List<ItemLatestResponse> itemList = itemRepository.getLatestProducts();
        return ResponseEntity.ok(itemList);
    }

    public ResponseEntity getItemsReponseSpecialSale() {
        List<ItemSpecialResponse> itemList = itemRepository.getSpecialSaleProducts();
        return ResponseEntity.ok(itemList);
    }

    public ResponseEntity getItemsResponsePopular() {
        List<ItemPopularResponse> itemList = itemRepository.getPopularProducts();
        return ResponseEntity.ok(itemList);
    }
}
