package com.website.common.repository.purchases;

import com.website.common.repository.purchases.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasesRepository extends JpaRepository<Purchases, Long>, CustomPurchasesRepository {

    List<Purchases> findByUserIdAndItemIdAndCreatedAtAfter(Long userId, Long itemId, LocalDateTime beforeTen);
}
