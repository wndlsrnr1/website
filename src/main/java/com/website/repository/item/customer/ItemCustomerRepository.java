package com.website.repository.item.customer;

import com.website.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCustomerRepository extends JpaRepository<Item, Long>, ItemCustomerCustomRepository {
}
