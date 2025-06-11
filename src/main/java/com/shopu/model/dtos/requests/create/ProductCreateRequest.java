package com.shopu.model.dtos.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductCreateRequest {
    private String brand;

    private String name;

    private String description;

    private float price;

    private float discount;

    private List<String> images;
}
