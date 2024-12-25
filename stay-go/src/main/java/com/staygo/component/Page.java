package com.staygo.component;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Page {
    public static Pageable getPageable(int pageSize) {
        return PageRequest.of(0, pageSize);
    }
}
