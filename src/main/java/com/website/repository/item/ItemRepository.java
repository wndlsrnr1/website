package com.website.repository.item;

import com.website.repository.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
}
