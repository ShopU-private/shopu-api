package com.shopu.model.entities;

import com.shopu.model.enums.Category;
import com.shopu.model.enums.DosageForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;

    private String brand;

    private String name;

    private String manufacturerName;

    @Indexed
    private Category category;

    private DosageForm dosageForm;

    private String strength;

    private String quantity;

    private boolean prescriptionRequired;

    private int stock;

    private String description;

    private float price;

    private float discount;

    private List<String> images;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}

//    private <Type> reviews;
