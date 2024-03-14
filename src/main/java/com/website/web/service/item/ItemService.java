package com.website.web.service.item;

import com.website.domain.attachment.Attachment;
import com.website.domain.category.Subcategory;
import com.website.domain.item.Item;
import com.website.domain.item.ItemAttachment;
import com.website.domain.item.ItemSubcategory;
import com.website.domain.item.ItemThumbnail;
import com.website.repository.attachment.AttachmentRepository;
import com.website.repository.item.attachment.ItemAttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.repository.item.subcategory.ItemSubcategoryRepository;
import com.website.repository.item.thumbnail.ItemThumbnailRepository;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.item.EditItemRequest;
import com.website.web.dto.request.item.SaveItemRequest;
import com.website.web.dto.response.item.CarouselItemResponse;
import com.website.web.dto.response.item.ItemDetailResponse;
import com.website.web.dto.response.item.ItemResponse;
import com.website.web.dto.sqlcond.item.ItemSearchCond;
import com.website.web.service.attachment.FileService;
import com.website.web.service.common.BindingResultUtils;
import com.website.web.service.item.carousel.ItemHomeCarouselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    //private final BindingResultUtils bindingResultUtils;
    private final MessageSource messageSource;
    private final FileService fileService;
    private final AttachmentRepository attachmentRepository;
    private final ItemAttachmentRepository itemAttachmentRepository;
    private final ItemSubcategoryRepository itemSubcategoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BindingResultUtils bindingResultUtils;
    private final ItemHomeCarouselService itemHomeCarouselService;
    private final ItemThumbnailRepository itemThumbnailRepository;


    public ResponseEntity sendItemResponseByCond(ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable) {
        //바이딩 에러
        if (bindingResult.hasErrors()) {
            ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(new ApiError(bindingResult)).data(null).message("binding error").build();
            return ResponseEntity.badRequest().body(body);
        }

        //서비스 에러
        if (itemSearchCond.getPriceMin() != null && itemSearchCond.getPriceMax() != null) {

            if (itemSearchCond.getPriceMin() > itemSearchCond.getPriceMax()) {
                String message = messageSource.getMessage("InvalidRange", null, null);
                ApiResponseBody<Object> body = ApiResponseBody.builder()
                        .apiError(new ApiError("priceMin", message))
                        .data(null)
                        .message("has error").build();
                return ResponseEntity.badRequest().body(body);
            }
        }

        if (itemSearchCond.getQuantityMin() != null && itemSearchCond.getQuantityMax() != null) {
            if (itemSearchCond.getQuantityMin() > itemSearchCond.getQuantityMax()) {
                String message = messageSource.getMessage("InvalidRange", null, null);
                ApiResponseBody<Object> body = ApiResponseBody.builder()
                        .apiError(new ApiError("quantityMin", message))
                        .data(null)
                        .message("has error").build();
                return ResponseEntity.badRequest().body(body);
            }
        }

        //정상 흐름
        Page<ItemResponse> itemResponseList = itemRepository.getItemResponseByCond(itemSearchCond, pageable);
        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(itemResponseList)
                .apiError(null)
                .message("ok").build();
        return ResponseEntity.ok(body);
    }

    public ResponseEntity sendItemDetailPageByItemId(Long itemId) {
        //잘못된 값
        if (itemId == null || itemId < 0) {
            String message = messageSource.getMessage("Min", new Long[]{itemId, 0L}, null);
            ApiError error = new ApiError("id", message);
            ApiResponseBody<Object> bod = ApiResponseBody.builder()
                    .data(null)
                    .message(message)
                    .apiError(error)
                    .build();
            return ResponseEntity.badRequest().body(bod);
        }

        //DB에 없음
        Item findItem = itemRepository.findById(itemId).orElse(null);
        if (findItem == null) {
            String message = messageSource.getMessage("Nodata", null, null);
            ApiError error = new ApiError("id", message);
            ApiResponseBody<Object> body = ApiResponseBody.builder()
                    .data(null)
                    .message(message)
                    .apiError(error)
                    .build();
            return ResponseEntity.badRequest().body(body);
        }

        //정상 흐름
        List<ItemDetailResponse> itemDetailResponse = itemRepository.findItemDetailResponse(itemId);

        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(itemDetailResponse)
                .apiError(null)
                .message("ok")
                .build();

        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity saveItemByItemFormRequest(SaveItemRequest saveItemRequest, BindingResult bindingResult) {
        log.info("saveItemRequest = {}", saveItemRequest);
        //binding error

        //정합성 에러

        //그외 서비스 에러

        //정상흐름
        //Long itemId = saveItemRequest.getItemId();
        //Long categoryId = saveItemRequest.getCategoryId();

        //파리미터 정보 가져오기
        String name = saveItemRequest.getName();
        Integer price = saveItemRequest.getPrice();
        String description = saveItemRequest.getDescription();
        String nameKor = saveItemRequest.getNameKor();
        Integer quantity = saveItemRequest.getQuantity();
        String status = saveItemRequest.getStatus();
        LocalDateTime releasedAt = saveItemRequest.getReleasedAt();
        Long subcategoryId = saveItemRequest.getSubcategoryId();

        //아이템 저장
        Item item = new Item(name, nameKor, price, quantity, status, description, releasedAt);
        itemRepository.save(item);

        List<String> images = saveItemRequest.getImages();
        List<MultipartFile> imageFiles = saveItemRequest.getImageFiles();
        List<Attachment> attachmentList = new ArrayList<>();

        MultipartFile thumbnailFile = saveItemRequest.getThumbnailFile();


        //파일 저장
        //1. 그냥 이미지 파일 저장
        if (imageFiles != null) {
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile file = imageFiles.get(i);
                String requestedName = images.get(i);

                //파일 정보 추출
                Attachment attachment = fileService.saveFile(requestedName, file);
                attachmentList.add(attachment);
            }
        }


        //2. 썸네일 저장

        if (saveItemRequest.getThumbnailFile() != null) {
            String requestedNameOfThumbnail = thumbnailFile.getOriginalFilename();
            Attachment attachmentOfThumbnail = fileService.saveFile(requestedNameOfThumbnail, thumbnailFile);
            //파일 정보 저장
            attachmentRepository.saveAll(attachmentList);
            attachmentRepository.save(attachmentOfThumbnail);

            for (Attachment attachment : attachmentList) {
                itemAttachmentRepository.save(new ItemAttachment(item, attachment));
            }
            itemAttachmentRepository.save(new ItemAttachment(item, attachmentOfThumbnail));

            itemThumbnailRepository.save(new ItemThumbnail(item, attachmentOfThumbnail));
        }


        //조인 테이블에 저장
        Subcategory subcategory = subcategoryRepository.findById(subcategoryId).orElse(null);
        log.info("subcategory = {}", subcategory);
        itemSubcategoryRepository.save(new ItemSubcategory(item, subcategory));


        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(null)
                .message("ok")
                .apiError(null).build();

        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity removeItemByItemId(Long itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return ResponseEntity.badRequest().build();
        }
        log.info("itemId = {}", itemId);


        //item에 붙어 있는 파일 삭제
        List<ItemAttachment> itemAttachmentList = itemAttachmentRepository.findByItemId(itemId);
        log.info("itemAttachmentList = {}", itemAttachmentList);
        List<Long> attachmentIdList = new ArrayList<Long>();
        for (ItemAttachment itemAttachment : itemAttachmentList) {
            Long attachmentId = itemAttachment.getAttachment().getId();
            attachmentIdList.add(attachmentId);
            log.info("attachmentId = {}", attachmentId);
        }
        log.info("attachmentIdList = {}", attachmentIdList);
        fileService.deleteFilesAndDbData(attachmentIdList);

        //조인 테이블 삭제
        itemSubcategoryRepository.deleteByItemId(itemId);
        itemAttachmentRepository.deleteByItemId(itemId);
        itemThumbnailRepository.deleteByItemId(itemId);
        itemRepository.deleteById(itemId);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity deleteFileOnItem(List<Long> fileIdList) {
        //DB에서 삭제
        itemRepository.deleteFileOnItem(fileIdList);

        //파일 삭제
        List<Attachment> attachmentList = attachmentRepository.getListByIdList(fileIdList);

        fileService.deleteFiles(attachmentList);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity editItemFormOnAdmin(Long itemId, EditItemRequest editItemRequest, BindingResult bindingResult) {
        log.info("editItemRequest = {}", editItemRequest);
        if (itemId == null) {
            ApiResponseBody<Object> body = ApiResponseBody.builder()
                    .apiError(new ApiError("itemId", messageSource.getMessage("Nodata", null, null)))
                    .message("badRequest").build();
            return ResponseEntity.badRequest().body(body);
        }

        if (bindingResult.hasErrors()) {
            ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(new ApiError(bindingResult)).build();
            return ResponseEntity.badRequest().body(body);
        }

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            ApiResponseBody<Object> body = ApiResponseBody.builder()
                    .apiError(new ApiError("itemId", messageSource.getMessage("Nodata", null, null)))
                    .message("badRequest").build();
            return ResponseEntity.badRequest().body(body);
        }

        //파일 업데이트
        itemRepository.updateItemByDto(itemId, editItemRequest);

        //서브카테고리 정리
        Long subcategoryId = editItemRequest.getSubcategoryId();
        itemSubcategoryRepository.updateSubcategory(itemId, subcategoryId);

        //사진 업로드: 예외를 발생시키고 로그를 찍어야 할 것 같음.
        List<String> images = editItemRequest.getImages();
        List<MultipartFile> imageFiles = editItemRequest.getImageFiles();
        if (images != null && imageFiles != null) {
            fileService.uploadFile(images, imageFiles, item);
        }


        //사진 지우기, 연관관계 지우기, 실제 파일 지우기
        List<Long> imagesForDelete = editItemRequest.getImagesForDelete();
        fileService.deleteFilesAndDbData(imagesForDelete);

        //연관관계 업데이트
        Long thumbnailId = editItemRequest.getThumbnailId();
        ItemAttachment itemAttachmentForThumbnail = itemAttachmentRepository.findByItemIdAndAttachmentId(itemId, thumbnailId);
        Attachment attachmentForThumbnail = itemAttachmentForThumbnail.getAttachment();

        if (itemAttachmentForThumbnail == null) {
            return ResponseEntity.badRequest().build();
        }

        for (Long imageIdForDelete : imagesForDelete) {
            if (imageIdForDelete.equals(thumbnailId)) {
                return ResponseEntity.badRequest().build();
            }
        }

        ItemThumbnail itemThumbnail = itemThumbnailRepository.findByItemId(itemId);
        if (itemThumbnail == null) {
            itemThumbnailRepository.save(new ItemThumbnail(item, attachmentForThumbnail));
        }
        itemThumbnailRepository.updateByItemIdAndAttachmentId(itemId, thumbnailId);

        //정상 흐름
        return ResponseEntity.ok(ApiResponseBody.builder().message("ok").build());
    }

    public ResponseEntity<List<CarouselItemResponse>> getCarouselItemsInHome() {
        itemHomeCarouselService.getCarouselsByCond(null, null, null);
        return itemRepository.getCarouselItemsInHome();
    }
}
