package com.shopu.model.dtos.response;

import com.shopu.model.dtos.requests.create.CompositionItem;
import com.shopu.model.enums.Category;
import com.shopu.model.enums.DosageForm;
import com.shopu.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    // TODO Currently We do not use this DTO
    private String id;
    private String name;
    private String brand;
    private ProductType productType; // new
    private String manufacturerName;

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

    private String storageInfo;        // e.g., "Store in cool dry place"
    private String disclaimer;

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
}
