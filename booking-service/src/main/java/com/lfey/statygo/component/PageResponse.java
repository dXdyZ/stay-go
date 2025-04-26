package com.lfey.statygo.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public final class PageResponse<T> {
    //Данные текущей страницы
    private List<T> content;
    //Текущий номер страницы
    private int currentPage;
    //Общее кол-во страниц
    private int totalPages;
    //Общее кол-во элементов во всех страницах
    private long totalItems;

    public static <T, U> PageResponse<T> fromPage(Page<U> page, Function<U, T> convert) {
        return new PageResponse<>(
                page.getContent().stream().map(convert).toList(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
