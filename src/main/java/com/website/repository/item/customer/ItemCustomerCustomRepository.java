package com.website.repository.item.customer;

import com.website.web.dto.response.item.home.ItemsForCustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomerCustomRepository {
    Page<ItemsForCustomerResponse> getItemsForCustomerResponseByCondByLastItemId(Long subcategoryId, String sortedBy, Long totalItems, Long lastItemId, Pageable pageable);
}
