package com.shopu.model.dtos.response;
import com.shopu.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private String id;
    private String phoneNumber;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastSignedAt;
}
