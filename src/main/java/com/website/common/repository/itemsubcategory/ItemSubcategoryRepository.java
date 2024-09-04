package com.website.common.repository.itemsubcategory;

import com.website.common.repository.model.item.ItemSubcategory;
import com.website.common.repository.item.ItemSubcategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemSubcategoryRepository extends JpaRepository<ItemSubcategory, Long>, ItemSubcategoryCustomRepository {
}
