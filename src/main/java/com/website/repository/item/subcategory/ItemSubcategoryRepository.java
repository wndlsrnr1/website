package com.website.repository.item.subcategory;

import com.website.domain.item.ItemSubcategory;
import com.website.repository.item.subcategory.ItemSubcategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemSubcategoryRepository extends JpaRepository<ItemSubcategory, Long>, ItemSubcategoryCustomRepository {
}
