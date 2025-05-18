package com.example.staygoclient.dto;

import lombok.Data;

import java.util.List;

@Data
public final class PageResponse<T> {
    //Данные текущей страницы
    private List<T> content;
    //Текущий номер страницы
    private int currentPage;
    //Общее кол-во страниц
    private int totalPages;
    //Общее кол-во элементов во всех страницах
    private long totalItems;
}
