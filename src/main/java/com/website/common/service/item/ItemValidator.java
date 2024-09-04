package com.website.common.service.item;

import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.model.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemValidator {
    private final ItemRepository itemRepository;

    /**
     * checks null and exists. and return Item
     *
     * @param itemId
     * @return Entity of Item
     */
    public Item validateAndGet(Long itemId) {
        if (itemId == null) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "itemId is null");
        }
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "item is not found. itemId = " + itemId)
        );
    }

    public void validateItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "item not found. itemId = " + itemId);
        }
    }
}
