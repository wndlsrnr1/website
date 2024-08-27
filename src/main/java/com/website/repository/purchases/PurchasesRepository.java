package com.website.repository.purchases;

import com.website.repository.purchases.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasesRepository extends JpaRepository<Purchases, Long>, CustomPurchasesRepository {


}
