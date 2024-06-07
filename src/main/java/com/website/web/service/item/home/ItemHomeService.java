package com.website.web.service.item.home;

import com.website.domain.item.Item;
import com.website.repository.item.ItemRepository;
import com.website.web.dto.response.item.home.ItemLatestResponse;
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

    public ResponseEntity getItemLatest() {

        List<ItemLatestResponse> itemList = itemRepository.getLatestProducts();

        return ResponseEntity.ok(itemList);
    }
}
