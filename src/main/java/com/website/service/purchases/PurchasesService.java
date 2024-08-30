package com.website.service.purchases;

import com.website.repository.common.PageResult;
import com.website.repository.purchases.PurchasesRepository;
import com.website.repository.purchases.model.Purchases;
import com.website.repository.purchases.model.PurchasesSearch;
import com.website.repository.purchases.model.PurchasesSearchCriteria;
import com.website.service.common.model.PageResultDto;
import com.website.service.purchases.model.PurchasesSearchRequestDto;
import com.website.service.purchases.model.PurchasesSearchResponseDto;
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
