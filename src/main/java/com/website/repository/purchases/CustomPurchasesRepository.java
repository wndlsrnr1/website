package com.website.repository.purchases;

import com.website.repository.common.PageResult;
import com.website.repository.purchases.model.Purchases;
import com.website.repository.purchases.model.PurchasesSearchCriteria;

public interface CustomPurchasesRepository {
    PageResult<Purchases> search(PurchasesSearchCriteria criteria);
}
