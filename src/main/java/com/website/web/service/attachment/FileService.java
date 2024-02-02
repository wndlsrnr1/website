package com.website.web.service.attachment;

import com.website.domain.attachment.Attachment;
import com.website.repository.attachment.AttachmentRepository;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.dto.request.item.SaveItemRequest;
import com.website.web.dto.response.item.AttachmentByItemIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileService {

    private final AttachmentRepository attachmentRepository;

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
        return uuid + "." +ext;
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
}
