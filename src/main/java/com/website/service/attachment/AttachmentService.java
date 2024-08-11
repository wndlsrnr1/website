package com.website.service.attachment;

import com.website.repository.model.item.Item;
import com.website.repository.attachment.AttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.controller.api.model.common.ApiResponseBody;
import com.website.controller.api.model.response.attachment.ItemImageInfoForCustomerResponse;
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
