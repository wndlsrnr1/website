package com.website.repository.item.entitylisteners;

import com.website.repository.model.item.Item;
import com.website.repository.item.ItemAttachmentRepository;
import com.website.repository.itemsubcategory.ItemSubcategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PreRemove;


//나중에 사용하기 ㅠㅠ
@Slf4j
//@Repository
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
