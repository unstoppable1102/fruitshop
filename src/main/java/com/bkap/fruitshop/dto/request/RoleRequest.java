package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RoleRequest {
    @NotBlank(message = "Role name is required")
    private String name;
    private String description;

}
