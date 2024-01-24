package com.website.web.service.attachment;

import com.website.domain.attachment.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class SaveFileService {

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
}
