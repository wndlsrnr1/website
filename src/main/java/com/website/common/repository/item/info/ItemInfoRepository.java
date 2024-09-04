package com.website.common.repository.item.info;

import com.website.common.repository.model.item.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInfoRepository extends JpaRepository<ItemInfo, Long>, ItemInfoCustomRepository {
}
