package com.website.common.repository.item;

public interface ItemSubcategoryCustomRepository {
    void deleteByItemId(Long itemId);

    void updateSubcategory(Long itemId, Long subcategoryId);
}
