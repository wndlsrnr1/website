package com.website.web.dto.request.item;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releasedAt;
    @NotNull
    private Integer price;
    @NotNull
    private Integer quantity;
    private List<MultipartFile> imageFiles;
    private List<String> images;
    private String status;
    private String description;
    private List<Long> imagesForDelete;
    private Long imageIdForThumbnail;
    private Long thumbnailId;
}