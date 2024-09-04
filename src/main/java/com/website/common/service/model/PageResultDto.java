package com.website.common.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResultDto<T> {
    private List<T> items;
    private String nextSearchAfter;
    private Long totalCount;
}
