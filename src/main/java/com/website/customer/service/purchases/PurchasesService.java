package com.website.customer.service.purchases;

import com.website.common.repository.model.item.Item;
import com.website.common.repository.purchases.model.Purchases;
import com.website.common.repository.user.model.User;
import com.website.common.service.item.ItemValidator;
import com.website.customer.service.purchases.model.CreatePurchasesRequestDto;
import com.website.customer.service.purchases.model.PurchasesResponseDto;
import com.website.customer.service.purchases.model.PurchasesSearchRequestDto;
import com.website.customer.service.purchases.model.PurchasesSearchResponseDto;
import com.website.common.repository.common.PageResult;
import com.website.common.repository.purchases.PurchasesRepository;
import com.website.common.repository.purchases.model.PurchasesSearch;
import com.website.common.repository.purchases.model.PurchasesSearchCriteria;
import com.website.common.service.model.PageResultDto;
import com.website.customer.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchasesService {

    private final PurchasesRepository purchasesRepository;
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;

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

    @Transactional
    public PurchasesResponseDto create(CreatePurchasesRequestDto dto) {
        User user = userValidator.validateAndGet(dto.getUserId());
        Item item = itemValidator.validateAndGet(dto.getItemId());

        Purchases purchasesForSave = dto.toEntity(user, item);

        Purchases createdPurchases = purchasesRepository.save(purchasesForSave);
        return PurchasesResponseDto.of(createdPurchases);
    }
}
