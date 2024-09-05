package com.website.customer.service.purchases;

import com.website.common.exception.*;
import com.website.common.repository.item.ItemRepository;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.purchases.model.OrderStatus;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchasesService {

    private final PurchasesRepository purchasesRepository;
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;
    private final PurchasesValidator purchasesValidator;
    private final ItemRepository itemRepository;

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
    public PurchasesResponseDto createPurchases(CreatePurchasesRequestDto dto) {

        try {
            User user = userValidator.validateAndGet(dto.getUserId());
            Item item = itemValidator.validateAndGet(dto.getItemId());
            if (item.getQuantity() < dto.getTotalAmount()) {
                throw new OutOfStockException(ErrorCode.BAD_REQUEST,
                        "Item out of stock. itemId = " + item.getId() + " stock = " + item.getQuantity());
            }

            List<Purchases> existingOrder = purchasesRepository.findByUserIdAndItemIdAndCreatedAtAfter(
                    user.getId(),
                    item.getId(),
                    LocalDateTime.now().minusMinutes(5)
            );

            if (existingOrder.size() > 0) {
                throw new OrderAlreadyExistsException(
                        "purchases exists in five minutes. before orderId = "
                                + existingOrder.stream().map(Purchases::getId).collect(Collectors.toList()));
            }

            item.setQuantity(
                    item.getQuantity() - dto.getTotalAmount()
            );
            log.info("item = {}", item);
            itemRepository.save(item);

            boolean paymentSuccessful = processPayment(user, item, dto.getTotalAmount());
            if (!paymentSuccessful) {
                throw new PaymentFailedException("Payment processing failed. userId = " + user.getId() + ", itemId = " + item.getId());
            }

            Purchases purchasesForSave = dto.toEntity(user, item);
            Purchases createdPurchases = purchasesRepository.save(purchasesForSave);
            return PurchasesResponseDto.of(createdPurchases);

            //todo: implement simple payment system.

        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
            throw new ConcurrentUpdateException("Another transaction has updated this item");
        }

    }

    //implement: temporal
    private boolean processPayment(User user, Item item, Integer totalAmount) {
        return true;
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Purchases purchases = purchasesValidator.validateAndGet(orderId);

        if (!purchases.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException("Your are not authorized to cancel this order userId = " + userId);
        }

        if (purchases.getStatus() != OrderStatus.NEW && purchases.getStatus() != OrderStatus.PROCESSING) {
            //todo: 서비스에 따라 복잡해짐 -> 반품 신청 및 관리자 페이지로 ㄱ
            throw new OrderCannotBeCancelledException("Order cannot be cancelled in its state. State = " + purchases.getStatus().name());
        }

        if (LocalDateTime.now().minusHours(24).isAfter(purchases.getOrderDate())) {
            throw new PaymentRefundException("Refund process failed");
        }

        boolean refundSuccessful = processRefund(purchases);
        if (!refundSuccessful) {
            throw new PaymentRefundException("Refund process failed");
        }

        purchases.setStatus(OrderStatus.CANCELLED);
        purchasesRepository.save(purchases);


        revertInventory(purchases);
    }

    private void revertInventory(Purchases purchases) {
        //increase item quantity by request
        Item item = purchases.getItem();
        item.setQuantity(item.getQuantity() + purchases.getTotalAmount());
    }

    private boolean processRefund(Purchases order) {
        //need payment gate
        return true;
    }
}
