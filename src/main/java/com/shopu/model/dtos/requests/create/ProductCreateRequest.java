package com.shopu.model.dtos.requests.create;

import com.shopu.model.enums.Category;
import com.shopu.model.enums.DosageForm;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    @NotNull(message = "Name cannot be null")
    private String name;

//  @NotNull(message = "Brand cannot be null")
    private String brand;

    @NotNull(message = "Manufacturer Name cannot be null")
    private String manufacturerName;

    @NotNull(message = "Category cannot be null")
    private Category category;

    @NotNull(message = "Dosage form cannot be null")
    private DosageForm dosageForm;

    @NotNull(message = "Pack size cannot be null")
    private String packSize;

    private boolean prescriptionRequired;

    @NotNull(message = "stock cannot be null")
    private int stock;

    @NotNull(message = "description cannot be null")
    private String description;

    @Digits(integer = 10, fraction = 6)
    @DecimalMin(value = "0.0", message = "Amount should not be negative")
    private float price;

    @Digits(integer = 10, fraction = 6)
    @DecimalMin(value = "0.0", message = "Amount should not be negative")
    private float discount;

//  @NotNull(message = "Images cannot be null")
    private List<String> images;

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
}
