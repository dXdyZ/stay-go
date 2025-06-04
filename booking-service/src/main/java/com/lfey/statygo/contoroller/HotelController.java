package com.lfey.statygo.contoroller;

import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.contoroller.documentation.HotelControllerDocs;
import com.lfey.statygo.dto.CreateHotelDto;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.dto.HotelUpdateRequestDto;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.service.HotelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@Tag(name = "Hotel API", description = "Api for management hotel")
public class HotelController implements HotelControllerDocs {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHotel(@Valid @RequestBody CreateHotelDto createHotelDto) {
        hotelService.saveHotel(createHotelDto);
    }

    @PostMapping(value = "/{hotelId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addPhoto(@PathVariable Long hotelId,
                         @RequestParam(value = "mainPhoto", required = false) Integer mainPhoto,
                         @RequestParam("photos") List<MultipartFile> photos) {
        hotelService.addPhotoToHotel(photos, hotelId, mainPhoto);
    }

    @PatchMapping("/{id}")
    public void updateHotelData(@PathVariable Long id,
                                @Valid @RequestBody HotelUpdateRequestDto hotelUpdateRequest) {
        hotelService.updateHotelDataById(id, hotelUpdateRequest);
    }

    @GetMapping("/{id}/{guests}/{startDate}/{endDate}")
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
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) Integer stars,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(PageResponse.fromPage(hotelService.searchHotelByFilter(
                startDate, endDate, stars, country, city, page
        )));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotelById(@PathVariable Long id) {
        hotelService.deleteHotelById(id);
    }
}
