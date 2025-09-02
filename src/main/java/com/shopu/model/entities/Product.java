package com.shopu.model.entities;

import com.shopu.model.dtos.requests.create.CompositionItem;
import com.shopu.model.enums.Category;
import com.shopu.model.enums.DosageForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    // Product Attributes
    @Id
    private String id;
    @Indexed
    private String name;
    private String brand;
    private String manufacturerName;
    @Indexed
    private Category category;
    private DosageForm dosageForm;

    // Quantity Attributes
    private String strength;
    private String packSize;
    private boolean prescriptionRequired = false;

    private String description;
    private List<CompositionItem> composition;
    private String ingredients;

    private String uses;
    private String benefits;
    private String howToUse;
    private String sideEffects;
    private String precautions;
    private String safetyAdvice;
    private String safetyInformation;

    private String storageInfo;
    private String disclaimer;

    private String hsnCode;
    private String manufacturerDetails;
    private String countryOfOrigin;

    private LocalDate expiryDate;
    private LocalDate manufacturingDate;

    private float price;
    private float discount;
    private float averageRating;
    private int numberOfReviews;

    private List<String> images;
    private int stock;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
