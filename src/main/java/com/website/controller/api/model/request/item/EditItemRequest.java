package com.website.controller.api.model.request.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EditItemRequest {
    @NotNull
    @Min(value = 0)
    private Long subcategoryId;
    @NotEmpty
    private String nameKor;
    @NotEmpty
    private String name;
    //이거 무슨 의미임?
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releasedAt;
    @NotNull
    private Integer price;
    private Integer quantity;
    private List<MultipartFile> imageFiles;
    private List<String> images;
    private String status;
    private String description;
    private List<Long> imagesForDelete;
    private Long thumbnailId;
    private Integer saleRate;
    private String brand;
    private String manufacturer;
    private String madeIn;

    private Long imageIdForThumbnail;

}