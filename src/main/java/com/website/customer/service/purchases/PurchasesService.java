package com.website.customer.service.purchases;

import com.website.customer.service.purchases.model.PurchasesSearchRequestDto;
import com.website.customer.service.purchases.model.PurchasesSearchResponseDto;
import com.website.common.repository.common.PageResult;
import com.website.common.repository.purchases.PurchasesRepository;
import com.website.common.repository.purchases.model.PurchasesSearch;
import com.website.common.repository.purchases.model.PurchasesSearchCriteria;
import com.website.common.service.model.PageResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchasesService {

    private final PurchasesRepository purchasesRepository;

    public PageResultDto<PurchasesSearchResponseDto> search(PurchasesSearchRequestDto dto) {
        PurchasesSearchCriteria criteria = dto.toCriteria();

        PageResult<PurchasesSearch> search = purchasesRepository.search(criteria);

        PageResultDto<PurchasesSearchResponseDto> pageResultDto = PageResultDto.<PurchasesSearchResponseDto>builder()
                .items(search.getItems().stream().map(PurchasesSearchResponseDto::of).collect(Collectors.toList()))
                .nextSearchAfter(search.getNextSearchAfter())
                .totalCount(search.getTotalCount())
                .build();

        return pageResultDto;
    }
}
