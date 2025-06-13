package com.shopu.model.dtos.requests.create;

import com.shopu.model.enums.Category;
import com.shopu.model.enums.DosageForm;
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

    private String manufacturerName;

    private Category category;

    private DosageForm dosageForm;

    private String strength;

    private boolean prescriptionRequired;

    private int stock;

    private String description;

    private float price;

    private float discount;

    private List<String> images;
}
