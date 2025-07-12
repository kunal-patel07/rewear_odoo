package com.example.cafe_backedn.utils;

import com.example.cafe_backedn.advices.PaginationResponse;
import org.springframework.data.domain.Page;

public class PaginationUtil {

    public static <T> PaginationResponse<T> buildPageResponse(Page<T> page) {
        return new PaginationResponse<>(page);
    }
}
