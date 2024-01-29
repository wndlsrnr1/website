package com.website.web.dto.request.item;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaveItemRequest {
    private Long categoryId;
    private Long subcategoryId;
    private String nameKor;
    private String name;
    private LocalDateTime releasedAt;
    private Long price;
    private Long quantity;
    private List<MultipartFile> imageFiles;
    private List<String> images;
    private String status;
    private String description;
}
