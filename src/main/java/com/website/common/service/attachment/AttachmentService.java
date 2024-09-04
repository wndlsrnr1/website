package com.website.common.service.attachment;

import com.website.common.repository.model.item.Item;
import com.website.common.repository.attachment.AttachmentRepository;
import com.website.common.repository.item.ItemRepository;
import com.website.common.controller.model.ApiResponseBody;
import com.website.common.repository.attachment.model.ItemImageInfoForCustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    private final ItemRepository itemRepository;
    private final AttachmentRepository attachmentRepository;


    public ResponseEntity getResponseImageInfoForCustomerResponse(Long itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }

        Item findItem = itemRepository.findById(itemId).orElse(null);
        if (findItem == null) {
            return ResponseEntity.badRequest().build();
        }

        List<ItemImageInfoForCustomerResponse> result = attachmentRepository.getResponseImageInfoForCustomerResponse(itemId);
        return ResponseEntity.ok(ApiResponseBody.builder().data(result).build());
    }
}
