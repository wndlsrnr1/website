package com.website.common.service.attachment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.repository.model.attachment.Attachment;
import com.website.common.repository.model.item.Item;
import com.website.common.repository.model.item.ItemAttachment;
import com.website.common.repository.attachment.AttachmentRepository;
import com.website.common.repository.item.ItemAttachmentRepository;
import com.website.common.controller.model.ApiResponseBody;
import com.website.common.controller.item.model.SaveItemRequest;
import com.website.common.service.attachment.model.AttachmentByItemIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.website.common.repository.model.item.QItemAttachment.itemAttachment;


@Slf4j
@Component
@RequiredArgsConstructor
public class FileService {

    private final AttachmentRepository attachmentRepository;
    private final ItemAttachmentRepository itemAttachmentRepository;
    private final JPAQueryFactory query;

    @Value("${file.dir}")
    private String fileDir;

    public Attachment saveFile(String requestFileName, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String saveName = getFileName(originalFilename);
        try {
            multipartFile.transferTo(new File(fileDir + saveName));
        } catch (IOException e) {
            log.error("[IOException]", e);
            return null;
        }
        return new Attachment(requestFileName, saveName);
    }


    public ResponseEntity sendFilesByItemId(Long itemId) {
        //ItemId 널처리

        //Item Id가 없을 때
        List<Attachment> attachmentList = attachmentRepository.getAttachmentByItemId(itemId);
        List<AttachmentByItemIdResponse> responseList = new ArrayList<>();
        for (Attachment attachment : attachmentList) {
            AttachmentByItemIdResponse attachmentByItemIdResponse = new AttachmentByItemIdResponse(attachment.getId(), attachment.getSaveName(), attachment.getRequestName(), new File(fileDir + attachment.getSaveName()));
            responseList.add(attachmentByItemIdResponse);
        }

        //파일이 없을때

        //파일 불러오기

        ApiResponseBody<Object> body = ApiResponseBody.builder()
                .apiError(null)
                .data(responseList)
                .message("ok")
                .build();

        //파일 불러와서 ResponseBody에 넣기

        //전달하기
        return ResponseEntity.ok(body);
    }

    public ResponseEntity saveItemByForm(SaveItemRequest saveItemRequest) {
        return null;
    }

    private String getFileName(String requestFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = getExt(requestFileName);
        return uuid + "." + ext;
    }

    private String getExt(String name) {
        int point = getExtPoint(name);
        log.info("name = {}", point);
        return point == -1 ? "" : name.substring(point + 1);
    }

    private String getRequestName(String name) {
        int point = getExtPoint(name);
        return point == -1 ? name : name.substring(0, point);
    }

    private int getExtPoint(String name) {
        for (int i = name.length() - 1; i >= 0; i--) {
            char character = name.charAt(i);
            if (character == '.') {
                return i;
            }
        }
        return -1;
    }

    public void deleteFile(String fileName) {
        File file = new File(fileDir + fileName);
        boolean delete = file.delete();
    }

    public void deleteFiles(List<Attachment> attachmentList) {
        for (Attachment attachment : attachmentList) {
            String fileName = attachment.getSaveName();
            deleteFile(fileName);
        }
    }

    /**
     * delete 실패시에 로그 남길지 생각해보기
     */
    @Transactional
    public void deleteFilesAndDbData(List<Long> attachmentIdListForDelete) {
        if (attachmentIdListForDelete == null || attachmentIdListForDelete.size() == 0) {
            return;
        }

        List<Attachment> attachmentListForDelete = attachmentRepository.getListByIdList(attachmentIdListForDelete);

        //DB에서 먼저 삭제
        deleteFilesDbData(attachmentListForDelete);

        //File 삭제
        deleteFiles(attachmentListForDelete);
    }

    /**
     * @param attachment delete 실패시에 로그 남길지 생각해보기
     *                   DB와 파일 둘다 삭제
     */
    @Transactional
    public void deleteFileAndDbDatum(Attachment attachment) {
        if (attachment.getRequestName() == null || attachment.getId() == null) {
            return;
        }
    }

    /**
     * @param fileId DB에서만 삭제
     */
    @Transactional
    public void deleteFileDbDatum(Long fileId) {
        query.delete(itemAttachment).where(itemAttachment.attachment.id.eq(fileId)).execute();
        attachmentRepository.deleteById(fileId);
    }


    @Transactional
    public void deleteFilesDbData(List<Attachment> attachmentList) {
        for (Attachment attachment : attachmentList) {
            Long attachmentId = attachment.getId();
            deleteFileDbDatum(attachmentId);
        }
    }

    public List<Attachment> uploadFile(List<String> images, List<MultipartFile> imageFiles, Item item) {
        List<Attachment> attachmentList = new ArrayList<>();
        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile file = imageFiles.get(i);
            String requestedName = images.get(i);
            Attachment attachment = saveFile(requestedName, file);
            attachmentList.add(attachment);
        }

        List<Attachment> attachmentList1 = attachmentRepository.saveAll(attachmentList);
        for (Attachment attachment : attachmentList) {
            itemAttachmentRepository.save(new ItemAttachment(item, attachment));
        }

        return attachmentList1;
    }


}
