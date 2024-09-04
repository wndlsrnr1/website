package com.website.common.repository.item.customer;

import com.website.common.repository.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCustomerRepository extends JpaRepository<Item, Long>, ItemCustomerCustomRepository {
}
