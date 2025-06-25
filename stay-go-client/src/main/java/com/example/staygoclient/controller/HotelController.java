package com.example.staygoclient.controller;

import com.example.staygoclient.clietn.BookingClient;
import com.example.staygoclient.clietn.HotelClient;
import com.example.staygoclient.dto.BookingRoomDto;
import com.example.staygoclient.dto.HotelDto;
import com.example.staygoclient.dto.PageResponse;
import com.example.staygoclient.dto.RoomDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HotelController {
    private final HotelClient hotelClient;
    private final BookingClient bookingClient;

    public HotelController(HotelClient hotelClient, BookingClient bookingClient) {
        this.hotelClient = hotelClient;
        this.bookingClient = bookingClient;
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
        model.addAttribute("guests", guests);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "hotel-detail";
    }

    @GetMapping("/booking-confirm")
    public String showConfirmation(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("guests") Integer guests,
            @RequestParam Map<String, String> allParams,
            Model model) {

        // Получаем детали отеля
        HotelDto hotelDto = hotelClient.getHotelDetails(hotelId, guests, startDate, endDate);

        // Собираем выбранные комнаты в виде BookingRoomDto
        List<BookingRoomDto> bookingRooms = new ArrayList<>();
        double totalPrice = 0.0;

        // Ищем параметры вида "room_<roomType>"
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("room_")) {
                String roomType = entry.getKey().substring("room_".length());
                int quantity = Integer.parseInt(entry.getValue());

                if (quantity > 0) {
                    // Находим комнату для получения цены
                    for (RoomDto room : hotelDto.getRoomDto()) {
                        if (room.getRoomType().equals(roomType)) {
                            BookingRoomDto bookingRoom = BookingRoomDto.builder()
                                    .hotelId(hotelId)
                                    .startDate(startDate)
                                    .endDate(endDate)
                                    .roomType(roomType)
                                    .guests(guests)
                                    .numberOfRooms(quantity)
                                    .build();

                            bookingRooms.add(bookingRoom);
                            totalPrice += room.getTotalPrice() * quantity;
                            break;
                        }
                    }
                }
            }
        }

        // Создаем маппинг roomType -> description для отображения
        Map<String, String> roomDescriptions = hotelDto.getRoomDto().stream()
                .collect(Collectors.toMap(RoomDto::getRoomType, RoomDto::getDescription));

        model.addAttribute("hotel", hotelDto);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("guests", guests);
        model.addAttribute("bookingRooms", bookingRooms);
        model.addAttribute("roomDescriptions", roomDescriptions);
        model.addAttribute("totalPrice", totalPrice);

        return "booking-confirmation";
    }

    @PostMapping("/confirm-booking")
    public String confirmBooking(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("guests") Integer guests,
            @RequestParam Map<String, String> allParams) {

        List<BookingRoomDto> bookings = new ArrayList<>();

        int index = 0;
        while (true) {
            String roomTypeParam = "rooms[" + index + "].roomType";
            String numberOfRoomsParam = "rooms[" + index + "].numberOfRooms";

            if (!allParams.containsKey(roomTypeParam)) {
                break;
            }

            String roomType = allParams.get(roomTypeParam);
            String numberOfRoomsStr = allParams.get(numberOfRoomsParam);

            if (roomType != null && numberOfRoomsStr != null) {
                try {
                    int numberOfRooms = Integer.parseInt(numberOfRoomsStr);
                    BookingRoomDto room = BookingRoomDto.builder()
                            .hotelId(hotelId)
                            .startDate(startDate)
                            .endDate(endDate)
                            .guests(guests)
                            .roomType(roomType)
                            .numberOfRooms(numberOfRooms)
                            .build();
                    bookings.add(room);
                } catch (NumberFormatException e) {

                }
            }
            index++;
        }
        bookingClient.booking(bookings);
        return "redirect:/";
    }
}
