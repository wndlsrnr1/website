package com.website.repository.item.customer;

import com.website.repository.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCustomerRepository extends JpaRepository<Item, Long>, ItemCustomerCustomRepository {
}
