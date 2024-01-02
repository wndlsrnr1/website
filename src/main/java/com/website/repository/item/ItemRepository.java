package com.website.repository.item;

import com.website.domain.item.Item;

import java.util.List;

public interface ItemRepository {

    void saveItem(Item item);

    Item findItemById(Long id);

    List<Item> findAll();

    void updateNameById(String updateName, Long id);

    void deleteById(Long id);
}
