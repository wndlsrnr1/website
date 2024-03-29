package com.website.repository.item.entitylisteners;

import com.website.domain.item.Item;
import com.website.repository.item.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

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