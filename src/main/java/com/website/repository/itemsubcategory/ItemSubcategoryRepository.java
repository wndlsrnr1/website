package com.website.repository.itemsubcategory;

import com.website.repository.model.item.ItemSubcategory;
import com.website.repository.item.ItemSubcategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemSubcategoryRepository extends JpaRepository<ItemSubcategory, Long>, ItemSubcategoryCustomRepository {
}
