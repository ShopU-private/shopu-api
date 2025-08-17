package com.shopu.model.dtos.response;
import com.shopu.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductListResponse {
    private String id;
    private String name;
    private String image;
    private String description;
    private Category category;
    private float price;
    private int stock;
    private LocalDateTime createdAt;
}
