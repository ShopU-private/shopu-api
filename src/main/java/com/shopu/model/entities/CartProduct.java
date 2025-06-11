package com.shopu.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {

    @Id
    private String id;
    private String userId;
    private String productId;
    private int quantity;
    private LocalDateTime createdAt = LocalDateTime.now();
}
