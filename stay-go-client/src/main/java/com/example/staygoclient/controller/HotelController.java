package com.example.staygoclient.controller;

import com.example.staygoclient.clietn.HotelClient;
import com.example.staygoclient.dto.HotelDto;
import com.example.staygoclient.dto.PageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class HotelController {
    private final HotelClient hotelClient;

    public HotelController(HotelClient hotelClient) {
        this.hotelClient = hotelClient;
    }

    @GetMapping("/")
    public String showMain(Model model) {
        model.addAttribute("hotels", Collections.emptyList());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", 0);
        model.addAttribute("starsFilter", null);
        model.addAttribute("countryFilter", null);
        model.addAttribute("cityFilter", null);
        model.addAttribute("startDate");
        model.addAttribute("endDate");
        model.addAttribute("guests", 2);
        return "main";
    }

    /**
     * Обрабатывает запрос на поиск отелей и отображает результаты
     */
    @GetMapping("/search")
    public String searchHotels(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "guests") Integer guests,
            @RequestParam(name = "country") String country,
            @RequestParam(name = "city") String city,
            @RequestParam(name = "stars") Integer stars,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        try {
            PageResponse<HotelDto> response = hotelClient.searchHotel(startDate, endDate, country, city, stars, page);

            model.addAttribute("hotels", response.getContent());
            model.addAttribute("currentPage", response.getCurrentPage());
            model.addAttribute("totalPages", response.getTotalPages());
            model.addAttribute("totalItems", response.getTotalItems());

            model.addAttribute("countryFilter", country);
            model.addAttribute("cityFilter", city);
            model.addAttribute("starsFilter", stars);

            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("guests", guests);

        } catch (Exception e) {
            model.addAttribute("hotels", Collections.emptyList());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 1);
            model.addAttribute("totalItems", 0);

            model.addAttribute("countryFilter", country);
            model.addAttribute("cityFilter", city);
            model.addAttribute("starsFilter", stars);
        }
        return "search-result";
    }

    @GetMapping("/details")
    public String showDetails(@RequestParam("hotelId") Long hotelId,
            @RequestParam(name = "guests") Integer guests,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            Model model) {

        HotelDto hotelDto = hotelClient.getHotelDetails(hotelId, guests, startDate, endDate);

        model.addAttribute("hotel", hotelDto);
        return "hotel-detail";
    }
}
