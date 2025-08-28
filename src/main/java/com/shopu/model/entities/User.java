package com.shopu.model.entities;

import com.shopu.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
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
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;

    private String name;

    private String email;

    private String whatsappNumber;

    @Indexed(unique = true)
    @NotBlank(message = "Phone Field is Empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number Invalid")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    private Role role;

    private List<String> orderIds;
    private List<String> cartItemsId;
    private List<String> addressIds;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastSignedAt;
    // Those IDS who create or update
//    private String createdBy;
//    private String updatedBy;

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
        this.orderIds = new ArrayList<>();
        this.cartItemsId = new ArrayList<>();
        this.addressIds = new ArrayList<>();
    }
}
