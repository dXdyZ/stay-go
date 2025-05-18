package com.example.staygoclient.controller;

import com.example.staygoclient.clietn.HotelClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class HotelController {
    private final HotelClient hotelClient;

    public HotelController(HotelClient hotelClient) {
        this.hotelClient = hotelClient;
    }

    @GetMapping()
    public String showMain(Model model) {
        model.addAttribute("hotels", Collections.emptyList());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", 0);
        model.addAttribute("starsFilter", null);
        model.addAttribute("countryFilter", null);
        model.addAttribute("cityFilter", null);
        return "main";
    }
}
