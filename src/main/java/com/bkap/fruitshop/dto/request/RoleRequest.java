package com.bkap.fruitshop.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RoleRequest {
    private String name;
    private String description;

}
