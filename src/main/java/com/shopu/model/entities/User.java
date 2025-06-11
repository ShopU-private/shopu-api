package com.shopu.model.entities;

import com.shopu.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Phone Field is Empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number Invalid")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    @Field(name = "user_role")
    private Role role;

    private List<String> order_ids;
    private List<String> cart_items_id;
    private List<String> address_ids;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastSignedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
        this.order_ids = new ArrayList<>();
        this.cart_items_id = new ArrayList<>();
        this.address_ids = new ArrayList<>();
    }
}
