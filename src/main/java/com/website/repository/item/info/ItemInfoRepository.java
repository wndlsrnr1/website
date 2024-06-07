package com.website.repository.item.info;

import com.website.domain.item.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInfoRepository extends JpaRepository<ItemInfo, Long>, ItemInfoCustomRepository {
}
