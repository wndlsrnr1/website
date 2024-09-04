package com.website.common.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResultResponse<T> {
    private List<T> items;
    private String nextSearchAfter;
    private Long totalCount;
}
