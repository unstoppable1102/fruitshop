package com.bkap.fruitshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String shortDescription;
    private String image;
    private boolean status;


}
