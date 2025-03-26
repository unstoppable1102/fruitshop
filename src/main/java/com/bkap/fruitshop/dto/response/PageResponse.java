package com.bkap.fruitshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private int currentPage;      // Trang hiện tại
    private int pageSize;         // Số phần tử trên mỗi trang
    private long totalElements;   // Tổng số phần tử
    private int totalPages;       // Tổng số trang
    private boolean isLast;       // Có phải trang cuối không?
    private List<T> content;      // Dữ liệu thực tế

}