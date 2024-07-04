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
public class EditItemRequestV2 {
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
    private Integer quantity;
    private String status;
    private String description;
    private Integer saleRate;
    private String brand;
    private String manufacturer;
    private String madeIn;

    private List<Long> imageIdsForDelete;

    private List<Long> imageIdsForUpdate;
    private List<Integer> seqListForUpdate;

    private List<MultipartFile> imageFilesForUpload;
    private List<Integer> seqListForUpload;

    private Long imageIdForThumbnail;
    private Integer imageIndexForThumbnail;
}
