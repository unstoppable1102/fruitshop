package com.bkap.fruitshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private  String title;
    private  String shortDescription;
    private  String content;
    private MultipartFile image;
    private long postCategoryId;
    private boolean status;
}
