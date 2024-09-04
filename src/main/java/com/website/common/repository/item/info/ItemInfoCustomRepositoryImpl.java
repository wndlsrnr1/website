package com.website.common.repository.item.info;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.repository.item.info.model.ItemInfoEditRequest;
import com.website.common.repository.item.model.ItemInfoResponse;
import com.website.common.repository.item.model.QItemInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.website.common.repository.model.item.QItemInfo.itemInfo;


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

    @Override
    @Transactional
    public void updateItemInfo(String madeInParam, String brandParam, Integer saleRateParam, String manufacturerParam, Long itemId) {
        String madeIn = (madeInParam == null) ? "" : madeInParam;
        String brand = (brandParam == null) ? "" : brandParam;
        Integer saleRate = (saleRateParam == null) ? 0 : saleRateParam;
        String manufacturer = (manufacturerParam == null) ? "" : manufacturerParam;
        log.info("update 안돌아감");
        if (itemId == null) {
            log.error("item id is null");
            return;
        }

        query.
                update(itemInfo)
                .set(itemInfo.manufacturer, manufacturer)
                .set(itemInfo.madeIn, madeIn)
                .set(itemInfo.brand, brand)
                .set(itemInfo.salesRate, saleRate)
                .where(itemInfo.item.id.eq(itemId))
                .execute();
    }

    @Override
    public ItemInfoResponse findByItemId(Long itemId) {
        List<ItemInfoResponse> fetch = query
                .select(new QItemInfoResponse(itemInfo.id, itemInfo.item.id, itemInfo.views, itemInfo.brand, itemInfo.salesRate, itemInfo.madeIn, itemInfo.manufacturer))
                .from(itemInfo)
                .where(itemInfo.item.id.eq(itemId)).fetch();
        if (fetch.size() < 1) {
            return null;
        }
        return fetch.get(0);
    }
}
