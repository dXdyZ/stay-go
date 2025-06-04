package com.lfey.statygo.component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;


@Getter
@AllArgsConstructor
@Schema(
        description = "Page response",
        example = """
                {
                    "content": [
                        "hotelId": 1,
                        "grade": 4.5,
                        "stars": 5,
                        "name": "Hotel documentation",
                        "description": "This description of the hotel was created for documentation purposes",
                        "address": "12345, Russian, Moscow, Pushkina, 43a",
                        "roomDto": [
                            {
                                "roomType": "STANDARD",
                                "capacity": 4,
                                "totalPrice": 1234.5,
                                "bedType": "DOUBLE",
                                "roomSize": 34.3
                            }
                        ]
                    ],
                    "currentPage": 0,
                    "totalPages": 5,
                    "totalItems": 50
                }
                """
)
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
