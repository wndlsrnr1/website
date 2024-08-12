package com.website.repository.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PageResult<T> {
    private List<T> items;
    private String getNextSearchAfter;
    private Long totalCount;
}
