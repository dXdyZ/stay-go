package com.lfey.statygo.component;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPageable {
    public static Pageable getPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.unsorted());
    }
}
