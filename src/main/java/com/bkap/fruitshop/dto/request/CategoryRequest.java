package com.bkap.fruitshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.bkap.fruitshop.entity.Category}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest implements Serializable {
    private String name;
    private boolean status;
}