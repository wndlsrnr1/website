package com.website.common.controller.attachment;

import com.website.common.repository.model.attachment.Attachment;
import com.website.common.repository.attachment.AttachmentRepository;
import com.website.common.service.attachment.AttachmentService;
import com.website.common.service.attachment.FileService;
import com.website.common.service.BindingResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/attachment")
public class AttachmentController {

    private final FileService fileService;
    private final AttachmentRepository attachmentRepository;
    private final BindingResultUtils bindingResultUtils;
    private final AttachmentService attachmentService;

    @Value("${file.dir}")
    private String firDir;
    //
    ////파일 업로드
    //@PostMapping("/save/one")
    //public ResponseEntity requestUploadAttachment(AttachmentUploadRequest attachmentUploadRequest, BindingResult bindingResult) {
    //    log.info("attachmentUploadRequest = {}", attachmentUploadRequest);
    //    //에러가 있으면
    //    if (bindingResult.hasErrors()) {
    //        ApiResponseBody<Object> body = ApiResponseBody.builder()
    //                .apiError(new ApiError(bindingResult))
    //                .data(null)
    //                .message("BAD REQUEST")
    //                .build();
    //
    //        return ResponseEntity.badRequest().body(body);
    //    }
    //
    //    //null 처리
    //    String requestFileName = attachmentUploadRequest.getRequestFileName();
    //    MultipartFile multipartFile = attachmentUploadRequest.getMultipartFile();
    //    log.info("3");
    //    //if there is data inconsistency..
    //    Attachment attachment = fileService.saveFile(requestFileName, multipartFile);
    //    if (attachment == null) {
    //        //some action
    //    }
    //    log.info("4");
    //    Attachment savedAttachment = attachmentRepository.save(attachment);
    //
    //    return ResponseEntity.status(HttpStatus.CREATED).build();
    //}

    //파일 보내주기
    @GetMapping("/{id}")
    public ResponseEntity<Resource> responseFileToUser(@PathVariable("id") Long attachmentId) {

        //find from db
        Attachment attachment = attachmentRepository.findById(attachmentId).orElse(null);
        String saveName = attachment.getSaveName();
        String requestName = attachment.getRequestName();
        log.info("attachment = {}", attachment.getSaveName());

        //make resource
        UrlResource resource;
        try {
            resource = new UrlResource("file:"+firDir+saveName);
        } catch (MalformedURLException e) {
            log.error("[MalformedURLException]");
            return ResponseEntity.badRequest().build();
        }

        //setHeader
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(requestName, StandardCharsets.UTF_8).build();

        //response
        return ResponseEntity.ok()
                .header("Content-Disposition", contentDisposition.toString())
                .body(resource);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity responseImageInfoForCustomerResponse(@PathVariable("itemId") Long itemId) {
        return attachmentService.getResponseImageInfoForCustomerResponse(itemId);
    }


}
