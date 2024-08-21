package com.website.repository.purchases;

import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.purchases.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasesRepository extends JpaRepository<Purchases, Long>, CustomPurchasesRepository {

    List<Purchases> findPurchasesByUserAndItem(User user, Item item);

}
