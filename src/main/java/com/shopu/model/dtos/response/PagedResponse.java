package com.shopu.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedResponse<T> {
    // TODO create DTO for every Entity
    private List<T> content;
    private int pageNumber;
    private int totalPages;
    private boolean last;
    private boolean first;
}
