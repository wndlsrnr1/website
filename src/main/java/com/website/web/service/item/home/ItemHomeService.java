package com.website.web.service.item.home;

import com.website.domain.item.ItemThumbnail;
import com.website.repository.item.ItemAttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.repository.item.info.ItemInfoRepository;
import com.website.repository.item.thumbnail.ItemThumbnailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemHomeService {

    private final ItemRepository itemRepository;
    private final ItemInfoRepository itemInfoRepository;
    private final ItemAttachmentRepository itemAttachmentRepository;
    private final ItemThumbnailRepository itemThumbnailRepository;

    public ResponseEntity getRecentItem(Integer count) {

        return null;
    }
}
