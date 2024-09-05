package com.website.customer.service.purchases;

import com.website.common.exception.OrderNotFoundException;
import com.website.common.repository.purchases.PurchasesRepository;
import com.website.common.repository.purchases.model.Purchases;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchasesValidator {

    private final PurchasesRepository purchasesRepository;


    public Purchases validateAndGet(Long orderId) {
        return purchasesRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order not found. " + orderId));
    }
}
