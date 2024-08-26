package com.website.controller.admin.file;

import com.website.controller.api.model.request.item.SaveItemRequest;
import com.website.service.attachment.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
