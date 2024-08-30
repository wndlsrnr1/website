package com.website.repository.purchases;

import com.website.repository.common.PageResult;
import com.website.repository.purchases.model.Purchases;
import com.website.repository.purchases.model.PurchasesSearch;
import com.website.repository.purchases.model.PurchasesSearchCriteria;

public interface CustomPurchasesRepository {
    PageResult<PurchasesSearch> search(PurchasesSearchCriteria criteria);
}
