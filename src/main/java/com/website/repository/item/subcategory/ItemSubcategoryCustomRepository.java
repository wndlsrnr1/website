package com.website.repository.item.subcategory;

public interface ItemSubcategoryCustomRepository {
    void deleteByItemId(Long itemId);

    void updateSubcategory(Long itemId, Long subcategoryId);
}
