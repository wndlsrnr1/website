package com.website.repository.item.entitylisteners;

import com.website.common.repository.model.item.Item;
import com.website.common.repository.item.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
class ItemListenerTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ItemRepository itemRepository;

    @Transactional
    @Test
    void testNotNull() {
        Item item = itemRepository.findAll().get(0);
        itemRepository.delete(item);
    }

}