package com.website.web.dto.request.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EditItemRequest {
    private Long itemId;
    private Long subcategoryId;
    private String nameKor;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releasedAt;
    private Integer price;
    private Integer quantity;
    private List<MultipartFile> imageFiles;
    private List<String> images;
    private String status;
    private String description;
}