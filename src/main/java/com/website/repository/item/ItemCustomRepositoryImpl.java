package com.website.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.Item;
import com.website.domain.item.QItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.website.domain.item.QItem.*;

@Repository
public class ItemCustomRepositoryImpl implements ItemCustomRepository{

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    public ItemCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        //this.entityUpdater = new EntityUpdater(entityManager);
    }



    @Override
    public void updateNameById(String updateName, Long id) {
        jpaQueryFactory.update(item)
                .set(item.name, updateName)
                .where(item.id.eq(id));
    }

}
