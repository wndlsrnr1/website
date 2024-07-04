package com.website.web.service.item;

import com.website.domain.attachment.Attachment;
import com.website.domain.category.Subcategory;
import com.website.domain.item.*;
import com.website.repository.attachment.AttachmentRepository;
import com.website.repository.item.ItemAttachmentRepository;
import com.website.repository.item.ItemRepository;
import com.website.repository.item.info.ItemInfoRepository;
import com.website.repository.item.seq.ItemAttachmentSeqRepository;
import com.website.repository.item.thumbnail.ItemThumbnailRepository;
import com.website.repository.itemsubcategory.ItemSubcategoryRepository;
import com.website.repository.subcategory.SubcategoryRepository;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.item.EditItemRequest;
import com.website.web.dto.request.item.EditItemRequestV2;
import com.website.web.dto.request.item.SaveItemRequest;
import com.website.web.dto.response.item.*;
import com.website.web.dto.response.item.sequence.ItemAttachmentSequenceResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final ItemInfoRepository itemInfoRepository;
    private final ItemAttachmentSeqRepository itemAttachmentSeqRepository;


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
        MultipartFile thumbnailFile = saveItemRequest.getThumbnailFile();
        String thumbnailImage = saveItemRequest.getThumbnailImage();

        //itemInfo
        Integer saleRate = saveItemRequest.getSaleRate();
        String brand = saveItemRequest.getBrand();
        String manufacturer = saveItemRequest.getManufacturer();
        String madeIn = saveItemRequest.getMadeIn();


        //아이템 저장
        Item item = new Item(name, nameKor, price, quantity, status, description, releasedAt);
        itemRepository.save(item);

        List<String> images = saveItemRequest.getImages();
        List<MultipartFile> imageFiles = saveItemRequest.getImageFiles();
        List<Attachment> attachmentList = new ArrayList<>();

        if (imageFiles != null) {
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile file = imageFiles.get(i);
                String requestedName = file.getOriginalFilename();

                //파일 정보 추출
                Attachment attachment = fileService.saveFile(requestedName, file);
                attachmentList.add(attachment);
            }

            //파일 정보 저장
            attachmentRepository.saveAll(attachmentList);
            //log.info("attachmentList = {}", attachmentList);
            for (int i = 0; i < attachmentList.size(); i++) {
                Attachment attachment = attachmentList.get(i);
                ItemAttachment itemAttachment = itemAttachmentRepository.save(new ItemAttachment(item, attachment));
                itemAttachmentSeqRepository.save(new ItemAttachmentSeq(itemAttachment, i + 1));
            }
        }

        if (thumbnailFile != null) {
            Attachment thumbnailAttachment = fileService.saveFile(thumbnailImage, thumbnailFile);
            attachmentRepository.save(thumbnailAttachment);
            ItemAttachment itemAttachment = itemAttachmentRepository.save(new ItemAttachment(item, thumbnailAttachment));
            itemThumbnailRepository.save(new ItemThumbnail(thumbnailAttachment, item));
            itemAttachmentSeqRepository.save(new ItemAttachmentSeq(itemAttachment, 0));
        }


        //조인 테이블에 저장
        Subcategory subcategory = subcategoryRepository.findById(subcategoryId).orElse(null);
        log.info("subcategory = {}", subcategory);
        itemSubcategoryRepository.save(new ItemSubcategory(item, subcategory));
        itemInfoRepository.save(new ItemInfo(item, 0L, saleRate, brand, manufacturer, madeIn));
        //log.info("imageFileList = {}", imageFiles);
        for (MultipartFile imageFile : imageFiles) {
            log.info("imageFile = {}", imageFile.getOriginalFilename());
        }

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
        itemSubcategoryRepository.deleteByItemId(itemId);

        //파일 삭제, attachmet와 연관된 파일 삭제, 연관 관계 삭제
        List<Long> attachmentIdList = itemAttachmentRepository.findAttachmentIdByItemId(itemId);
        deleteImages(itemId, attachmentIdList);

        itemThumbnailRepository.deleteByItemId(itemId);
        itemRepository.deleteById(itemId);

        //item 삭제
        //조인 테이블 삭제
        return ResponseEntity.ok().build();
    }

    public ResponseEntity sendItemResponseByCondByLastItemId(
            ItemSearchCond itemSearchCond, BindingResult bindingResult, Pageable pageable, Long lastItemId, Integer lastPageNumber, Integer pageChunk, Boolean isLastPage
    ) {
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

        Page<ItemResponse> itemResponseList = null;
        if (isLastPage) {
            itemResponseList = itemRepository.getItemResponseByCondWhenLastPage(itemSearchCond, bindingResult, pageable, lastItemId, lastPageNumber, pageChunk, isLastPage);
        } else {
            itemResponseList = itemRepository.getItemResponseByCondByLastItemId(itemSearchCond, pageable, lastItemId, lastPageNumber, pageChunk);
        }

        //정상 흐름
        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .data(itemResponseList)
                .apiError(null)
                .message("ok").build();

        return ResponseEntity.ok(body);
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
        deleteImages(itemId, imagesForDelete);

        //정상 흐름
        return ResponseEntity.ok(ApiResponseBody.builder().message("ok").build());
    }

    @Transactional
    public ResponseEntity editItemFormOnAdminV2(Long itemId, EditItemRequestV2 editItemRequest, BindingResult bindingResult) {
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


        //아이템 업데이트
        itemRepository.updateItemByDto(itemId, editItemRequest);

        //서브카테고리 정리
        Long subcategoryId = editItemRequest.getSubcategoryId();
        itemSubcategoryRepository.updateSubcategory(itemId, subcategoryId);

        //파일 info DB에 저장하기
        itemInfoRepository.updateItemInfo(editItemRequest.getMadeIn(), editItemRequest.getBrand(), editItemRequest.getSaleRate(), editItemRequest.getManufacturer(), item.getId());

        //사진 업로드: 예외를 발생시키고 로그를 찍어야 할 것 같음.
        //파일 저장 -> attachment DB에 저장 -> itemAttachment 저장 -> itemAttachmentSeq 저장
        List<MultipartFile> imageFileListForUpload = editItemRequest.getImageFilesForUpload();
        List<Integer> seqListForUpload = editItemRequest.getSeqListForUpload();
        List<Attachment> uploadedAttachmentList = new ArrayList<>();
        Map<Long, Long> attachmentAndItemAttachmentMap = new HashMap<>();
        if (imageFileListForUpload != null && seqListForUpload != null && imageFileListForUpload.size() == seqListForUpload.size()) {
            for (int i = 0; i < imageFileListForUpload.size(); i++) {
                MultipartFile multipartFile = imageFileListForUpload.get(i);
                Integer sequence = seqListForUpload.get(i);
                Attachment attachment = fileService.saveFile(multipartFile.getOriginalFilename(), multipartFile);
                Attachment savedAttachment = attachmentRepository.save(attachment);
                uploadedAttachmentList.add(savedAttachment);
                ItemAttachment savedItemAttachment = itemAttachmentRepository.save(new ItemAttachment(item, savedAttachment));
                itemAttachmentSeqRepository.save(new ItemAttachmentSeq(savedItemAttachment, sequence));
                attachmentAndItemAttachmentMap.put(savedItemAttachment.getAttachment().getId(), savedItemAttachment.getId());
            }
        }

        //파일 지우기 -> attachment DB에서 지우기 -> itemAttachment 지우기 -> itemAttachmentSeq 지우기 (역순)

        List<Long> attachmentIdListForDelete = editItemRequest.getImageIdsForDelete();
        List<Attachment> attachmentForDelete = attachmentRepository.findAllById(attachmentIdListForDelete);
        fileService.deleteFiles(attachmentForDelete);

        for (int i = 0; i < attachmentIdListForDelete.size(); i++) {
            Long attachmentIdForDelete = attachmentIdListForDelete.get(i);
            List<ItemAttachment> itemAttachmentList = itemAttachmentRepository.findByAttachmentId(attachmentIdForDelete, itemId);
            for (int j = 0; j < itemAttachmentList.size(); j++) {
                ItemAttachment itemAttachment = itemAttachmentList.get(j);
                itemAttachmentSeqRepository.deleteByItemAttachmentId(itemAttachment.getId());
            }
            itemAttachmentRepository.deleteByAttachmentId(attachmentIdForDelete);
            attachmentRepository.deleteById(attachmentIdForDelete);
        }


        //사진 순서 update 하기
        List<Long> imageIdListForUpdate = editItemRequest.getImageIdsForUpdate();
        List<Integer> seqListForUpdate = editItemRequest.getSeqListForUpdate();

        if (imageIdListForUpdate != null && seqListForUpdate != null) {
            if (imageIdListForUpdate.size() != seqListForUpdate.size()) {
                return ResponseEntity.badRequest().build();
            }
            log.info("badRequest3");
            log.info("imageIdListForUpdate = {}", imageIdListForUpdate);
            log.info("seqListForUpdate = {}", seqListForUpdate);
            for (int i = 0; i < imageIdListForUpdate.size(); i++) {
                Long imageId = imageIdListForUpdate.get(i);
                Integer sequence = seqListForUpdate.get(i);
                ItemAttachment findItemAttachment = itemAttachmentRepository.findByItemIdAndAttachmentId(itemId, imageId);
                itemAttachmentSeqRepository.update(findItemAttachment.getId(), sequence);
            }
        }



        //썸네일 지정하기.
        Long imageIdForThumbnail = editItemRequest.getImageIdForThumbnail();
        Integer imageIndexForThumbnail = editItemRequest.getImageIndexForThumbnail();

        if (imageIdForThumbnail == null && imageIndexForThumbnail == null) {
            log.info("badRequest1");
            return ResponseEntity.badRequest().build();
        }

        if (imageIdForThumbnail != null && imageIndexForThumbnail != null) {
            log.info("badRequest2");
            return ResponseEntity.badRequest().build();
        }

        if (imageIdForThumbnail != null && imageIndexForThumbnail == null) {
            itemThumbnailRepository.updateThumbnail(itemId, imageIdForThumbnail);
            ItemAttachment findItemAttachment = itemAttachmentRepository.findByItemIdAndAttachmentId(itemId, imageIdForThumbnail);
            itemAttachmentSeqRepository.update(findItemAttachment.getId(), 0);
        }

        if (imageIdForThumbnail == null && imageIndexForThumbnail != null) {
            Attachment attachment = uploadedAttachmentList.get(imageIndexForThumbnail);
            itemThumbnailRepository.updateThumbnail(itemId, attachment.getId());
            //Long savedItemAttachmentId = attachmentAndItemAttachmentMap.get(imageIdForThumbnail);
            //itemAttachmentSeqRepository.update();

        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<CarouselItemResponse>> getCarouselItemsInHome() {
        itemHomeCarouselService.getCarouselsByCond(null, null, null);
        return itemRepository.getCarouselItemsInHome();
    }

    public ResponseEntity getThumbnailResponse(Long itemId) {
        if (itemId == null) {
            String message = messageSource.getMessage("Nodata", null, null);
            ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(new ApiError("itemId", message)).build();
            return ResponseEntity.badRequest().body(body);
        }

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            String message = messageSource.getMessage("Nodata", null, null);
            ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(new ApiError("itemId", message)).build();
            return ResponseEntity.badRequest().body(body);
        }

        ItemThumbnailResponse itemThumbnailResponse = itemThumbnailRepository.findByItemId(itemId);

        if (itemThumbnailResponse == null) {
            String message = messageSource.getMessage("Nodata", null, null);
            ApiResponseBody<Object> body = ApiResponseBody.builder().apiError(new ApiError("itemThumbnailId", message)).build();
            return ResponseEntity.badRequest().body(body);
        }

        ApiResponseBody<Object> body = ApiResponseBody.builder().data(itemThumbnailResponse).build();

        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity editThumbnail(Long itemId, Long imageId) {
        if (itemId == null || imageId == null) {
            return ResponseEntity.badRequest().build();
        }

        ItemThumbnailResponse itemThumbnail = itemThumbnailRepository.findByItemId(itemId);
        if (itemThumbnail == null) {
            return ResponseEntity.badRequest().build();
        }

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return ResponseEntity.badRequest().build();
        }

        Attachment attachment = attachmentRepository.findById(imageId).orElse(null);
        if (attachment == null) {
            return ResponseEntity.badRequest().build();
        }

        //ItemThumbnail itemThumbnail = itemThumbnailRepository.findByItemId(itemId);
        //log.info("itemThumbnail = {}", itemThumbnail);
        //
        //if (itemThumbnail == null) {
        //    return ResponseEntity.badRequest().build();
        //}

        itemThumbnailRepository.updateThumbnail(itemId, imageId);
        return ResponseEntity.ok().build();
    }

    private void deleteImages(Long itemId, List<Long> imagesForDelete) {
        deleteThumbnailInDb(itemId, imagesForDelete);
        fileService.deleteFilesAndDbData(imagesForDelete);
    }

    public ResponseEntity addThumbnail(Long itemId, Long imageId) {

        ItemAttachment itemAttachment = itemAttachmentRepository.findByItemIdAndAttachmentId(itemId, imageId);
        if (itemAttachment == null) {
            log.info("null");
            return ResponseEntity.badRequest().build();
        }
        log.info("do insert");

        itemThumbnailRepository.save(
                new ItemThumbnail(itemAttachment.getAttachment(), itemAttachment.getItem())
        );

        return ResponseEntity.ok().build();
    }

    private void deleteThumbnailInDb(Long itemId, List<Long> imagesForDelete) {
        ItemThumbnailResponse itemThumbnail = itemThumbnailRepository.findByItemId(itemId);
        if (itemThumbnail == null) {
            log.info("itemThumbnail is null");
            return;
        }
        Long fileId = itemThumbnail.getFileId();
        if (fileId != null && imagesForDelete.contains(fileId)) {
            itemThumbnailRepository.deleteById(itemThumbnail.getItemThumbnailId());
        }
    }

    //생성자가 없어서 쿼리를 만들 수 없었음 -> query projection 사용함.
    //
    public ResponseEntity getResponseItemInfo(Long itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }
        ItemInfoResponse itemInfo = itemInfoRepository.findByItemId(itemId);
        if (itemInfo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(ApiResponseBody.builder().data(itemInfo).build());
    }

    public ResponseEntity getItemAttachmentSequence(Long itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<ItemAttachmentSequenceResponse> attachmentSequenceList = itemAttachmentSeqRepository.findByItemId(itemId);
        Map<Long, Integer> attachmentSequenceMap = new HashMap<>();
        for (ItemAttachmentSequenceResponse itemAttachmentSequenceResponse : attachmentSequenceList) {
            Long fileId = itemAttachmentSequenceResponse.getFileId();
            Integer sequence = itemAttachmentSequenceResponse.getSequence();
            attachmentSequenceMap.put(fileId, sequence);
        }

        return ResponseEntity.ok(ApiResponseBody.builder().data(attachmentSequenceMap).build());
    }

    public ResponseEntity getItemBasicResponse(Long itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }
        ItemBasicResponse itemDefaultResponse = itemRepository.findItemBasicResponseByItemId(itemId);
        return ResponseEntity.ok(ApiResponseBody.builder().data(itemDefaultResponse).build());
    }
}
