package com.website.repository.item.customer;

import com.website.web.dto.response.item.home.ItemLatestResponse;
import com.website.web.dto.response.item.home.ItemPopularResponse;
import com.website.web.dto.response.item.home.ItemSpecialResponse;
import com.website.web.dto.response.item.home.ItemsForCustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemCustomerCustomRepository {
    Page<ItemsForCustomerResponse> getItemsForCustomerResponseByCondByLastItemId(Long subcategoryId, String sortedBy, Long totalItems, Long lastItemId, Pageable pageable);

    List<ItemLatestResponse> getLatestProducts();

    List<ItemSpecialResponse> getSpecialSaleProducts();

    List<ItemPopularResponse> getPopularProducts();
}