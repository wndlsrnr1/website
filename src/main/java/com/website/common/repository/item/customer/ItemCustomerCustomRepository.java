package com.website.common.repository.item.customer;

import com.website.common.repository.item.home.model.ItemLatestResponse;
import com.website.common.repository.item.home.model.ItemPopularResponse;
import com.website.common.repository.item.home.model.ItemSpecialResponse;
import com.website.common.repository.item.home.model.ItemsForCustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemCustomerCustomRepository {
    Page<ItemsForCustomerResponse> getItemsForCustomerResponseByCondByLastItemId(Long subcategoryId, String sortedBy, Long totalItems, Long lastItemId, Pageable pageable);

    List<ItemLatestResponse> getLatestProducts();

    List<ItemSpecialResponse> getSpecialSaleProducts();

    List<ItemPopularResponse> getPopularProducts();
}
