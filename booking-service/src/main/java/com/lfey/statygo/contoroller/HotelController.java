package com.lfey.statygo.contoroller;

import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.dto.CreateHotel;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.dto.HotelUpdateRequest;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/create")
    public void createHotel(@Valid @RequestBody CreateHotel createHotel) {
        hotelService.saveHotel(createHotel);
    }

    @PatchMapping("/{id}")
    public void updateHotelData(@PathVariable Long id,
                                @Valid @RequestBody HotelUpdateRequest hotelUpdateRequest) {
        hotelService.updateHotelDataById(id, hotelUpdateRequest);
    }

    @GetMapping("/users/{id}/{guests}/{startDate}/{endDate}")
    public ResponseEntity<HotelDto> getHotelByIdFindUser(
            @PathVariable Long id, @PathVariable Integer guests,
            @PathVariable String startDate, @PathVariable String endDate
    ) {
        return ResponseEntity.ok(hotelService.getHotelByIdUser(id, guests, startDate, endDate));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<HotelDto>> searchHotels(
            @RequestParam(required = false) Integer stars,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(PageResponse.fromPage(hotelService.searchHotelByFilter(
                stars, country, city, page)));
    }

    @DeleteMapping("/{id}")
    public void deleteHotelById(@PathVariable Long id) {
        hotelService.deleteHotelById(id);
    }
}
