package com.website.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.Item;
import com.website.domain.item.QItem;
import com.website.repository.utils.EntityUpdater;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ItemRepositoryWithJpaAndQueryDsl implements ItemRepository{

    private final EntityManager entityManager;
    private final ItemJpaRepository itemJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    //private final EntityUpdater entityUpdater;

    public ItemRepositoryWithJpaAndQueryDsl(EntityManager entityManager, ItemJpaRepository itemJpaRepository) {
        this.entityManager = entityManager;
        this.itemJpaRepository = itemJpaRepository;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        //this.entityUpdater = new EntityUpdater(entityManager);
    }

    @Override
    public void saveItem(Item item) {
        itemJpaRepository.save(item);
    }

    @Override
    public Item findItemById(Long id) {
        return itemJpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Item> findAll() {
        return itemJpaRepository.findAll();
    }

    @Override
    public void updateNameById(String updateName, Long id) {
        Item item = itemJpaRepository.findById(id).orElseGet(null);
        if (item != null) {
            item.setName(updateName);
            itemJpaRepository.save(item);
        }
    }

    @Override
    public void deleteById(Long id) {
        itemJpaRepository.deleteById(id);
        return;
    }

}
