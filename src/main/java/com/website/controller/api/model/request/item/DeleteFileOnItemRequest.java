package com.website.controller.api.model.request.item;

import lombok.Data;

import java.util.List;

@Data
public class DeleteFileOnItemRequest {
    private Long itemId;
    private List<Long> fileIdList;
}
