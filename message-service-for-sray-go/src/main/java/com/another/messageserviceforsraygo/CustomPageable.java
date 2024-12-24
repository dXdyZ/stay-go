package com.another.messageserviceforsraygo;

import org.springframework.data.domain.Pageable;

public class CustomPageable {

    public static Pageable getPageable(int size) {
        return Pageable.ofSize(size);
    }
}
