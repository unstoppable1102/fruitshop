package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RoleNameRequest {
    @NotBlank(message = "Role name is required")
    private Set<String> roleNames;


}
