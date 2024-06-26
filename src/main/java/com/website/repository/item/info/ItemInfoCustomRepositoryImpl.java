package com.website.repository.item.info;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.item.Item;
import com.website.domain.item.ItemInfo;
import com.website.domain.item.QItemInfo;
import com.website.repository.item.ItemRepository;
import com.website.web.dto.request.item.info.ItemInfoAddRequest;
import com.website.web.dto.request.item.info.ItemInfoEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.website.domain.item.QItemInfo.itemInfo;

@Slf4j
public class ItemInfoCustomRepositoryImpl implements ItemInfoCustomRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ItemInfoCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    @Transactional
    public void editItemInfo(ItemInfoEditRequest itemInfoEditRequest) {
        Long itemId = itemInfoEditRequest.getItemId();
        Long itemInfoId = itemInfoEditRequest.getItemInfoId();
        String manufacturer = itemInfoEditRequest.getManufacturer();
        Long views = itemInfoEditRequest.getViews();
        String madeIn = itemInfoEditRequest.getMadeIn();
        String brand = itemInfoEditRequest.getBrand();

        query.
                update(itemInfo)
                .set(itemInfo.manufacturer, manufacturer)
                .set(itemInfo.views, views)
                .set(itemInfo.madeIn, madeIn)
                .set(itemInfo.brand, brand)
                .where(itemInfo.id.eq(itemInfoId))
                .execute();
    }

    @Override
    @Transactional
    public void deleteItemInfoByItemId(Long itemId) {
        query.delete(itemInfo).where(itemInfo.item.id.eq(itemId)).execute();
    }
}
