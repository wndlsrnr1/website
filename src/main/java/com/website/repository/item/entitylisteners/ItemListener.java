package com.website.repository.item.entitylisteners;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.Item;
import com.website.repository.item.ItemAttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.repository.itemsubcategory.ItemSubcategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;

import static com.website.domain.item.QItemAttachment.*;
import static com.website.domain.item.QItemSubcategory.*;

//나중에 사용하기 ㅠㅠ
//@Repository
@Slf4j
public class ItemListener {

    @Autowired
    private ItemAttachmentRepository itemAttachmentRepository;
    @Autowired
    private ItemSubcategoryRepository itemSubcategoryRepository;

    public ItemListener() {
    }

    @PreRemove
    public void preRemove(Item item) {
        log.info("itemAttachmentRepository = {}", itemAttachmentRepository);
        log.info("itemSubcategoryRepository = {}", itemSubcategoryRepository);
        itemAttachmentRepository.deleteByItemId(item.getId());
        itemSubcategoryRepository.deleteByItemId(item.getId());
    }
}
