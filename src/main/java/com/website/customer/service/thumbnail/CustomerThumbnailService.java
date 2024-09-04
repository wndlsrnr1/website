package com.website.customer.service.thumbnail;

import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.item.thumbnail.ItemThumbnailRepository;
import com.website.common.repository.model.item.ItemThumbnail;
import com.website.customer.service.thumbnail.model.ThumbnailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerThumbnailService {

    private final ItemThumbnailRepository itemThumbnailRepository;

    @Transactional
    public ThumbnailResponseDto findByItem(Long itemId) {
        ItemThumbnail itemThumbnail = itemThumbnailRepository.findByItemId(itemId)
                .orElseThrow(() ->
                        new ClientException(ErrorCode.BAD_REQUEST, "itemThumbnail not found. itemId = " + itemId)
                );
        return ThumbnailResponseDto.of(itemThumbnail);
    }
}
