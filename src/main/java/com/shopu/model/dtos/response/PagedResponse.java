package com.shopu.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int totalPages;
    private boolean last;
    private boolean first;

    public PagedResponse(List<T> content, int pageNumber, int totalPages, boolean last, boolean first) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.last = last;
        this.first = first;
    }
}
