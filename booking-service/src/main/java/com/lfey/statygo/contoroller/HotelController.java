package com.lfey.statygo.contoroller;

import com.lfey.statygo.component.PageResponse;
import com.lfey.statygo.contoroller.documentation.HotelControllerDocs;
import com.lfey.statygo.dto.CreateHotelDto;
import com.lfey.statygo.dto.HotelDto;
import com.lfey.statygo.dto.HotelUpdateRequestDto;
import com.lfey.statygo.dto.ReviewDto;
import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.service.HotelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/hotels")
@Tag(name = "Hotel API", description = "Api for management hotel")
public class HotelController implements HotelControllerDocs {

    public final static String USER_PUBLIC_ID = "X-User-PublicId";

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

    @PutMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@RequestHeader(USER_PUBLIC_ID) UUID publicId, @Valid @RequestBody ReviewDto reviewDto) {
        hotelService.addReview(reviewDto, publicId);
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
            @NotBlank @RequestParam String startDate, @RequestParam(required = false) String endDate,

            @RequestParam(required = false) Integer stars,

            @NotBlank @RequestParam String country, @NotBlank @RequestParam String city,

            @Min(0) @Max(5) @RequestParam(required = false) Double grade,

            @RequestParam(required = false) String hotelType,

            @Min(0) @RequestParam(required = false) Double minPrice,
            @Min(0) @RequestParam(required = false) Double maxPrice,

            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(PageResponse.fromPage(hotelService.searchHotelByFilter(
                startDate, endDate, stars, country, grade, hotelType, minPrice, maxPrice, city, page
        )));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotelById(@PathVariable Long id) {
        hotelService.deleteHotelById(id);
    }
}
