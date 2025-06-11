package com.shopu.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    private String id;

    private String brand;

    private String name;

    private String description;

    private float price;

    private float discount;

    private List<String> images;

//    private <Type> reviews;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;


}
