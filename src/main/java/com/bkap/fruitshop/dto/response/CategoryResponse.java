package com.bkap.fruitshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.bkap.fruitshop.entity.Category}
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CategoryResponse implements Serializable {

    private Long id;
    private String name;
    private boolean status;
}