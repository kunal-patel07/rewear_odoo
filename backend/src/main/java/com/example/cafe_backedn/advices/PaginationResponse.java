package com.example.cafe_backedn.advices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaginationResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;
    private int skip;
    private boolean hasNext;

    public PaginationResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.lastPage = page.isLast();
        this.skip = page.getNumber() * page.getSize();
        this.hasNext = page.hasNext();
    }


}
