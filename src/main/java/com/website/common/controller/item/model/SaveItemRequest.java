package com.website.common.controller.item.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaveItemRequest {
    //private Long itemId;
    //private Long categoryId;
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
    private MultipartFile thumbnailFile;
    private String thumbnailImage;
    private Integer saleRate;
    private String brand;
    private String manufacturer;
    private String madeIn;

}
