package com.website.common.repository.item;

import com.website.common.repository.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, CustomItemRepository {
}
