package com.lfey.statygo.component;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CustomPageable {
    public static Pageable getPageable(int page) {
        return PageRequest.of(page, 5);
    }
}
