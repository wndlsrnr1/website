package com.website.web.controller.api.admin.file;

import com.website.web.dto.request.item.SaveItemRequest;
import com.website.web.service.attachment.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/file")
public class FileAdminController {

    private final FileService fileService;

    //미구현
    @GetMapping
    public ResponseEntity sendFilesByItemCond(@RequestParam(required = true) Long itemId) {
        return fileService.sendFilesByItemId(itemId);
    }


    //미구현
    @PostMapping("/admin/save")
    public ResponseEntity saveItemByRequest(@ModelAttribute SaveItemRequest saveItemRequest) {
        return fileService.saveItemByForm(saveItemRequest);
    }
}
