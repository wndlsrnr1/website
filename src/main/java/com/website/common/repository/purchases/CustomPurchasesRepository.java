package com.website.common.repository.purchases;

import com.website.common.repository.common.PageResult;
import com.website.common.repository.purchases.model.PurchasesSearch;
import com.website.common.repository.purchases.model.PurchasesSearchCriteria;

public interface CustomPurchasesRepository {
    PageResult<PurchasesSearch> search(PurchasesSearchCriteria criteria);
}
