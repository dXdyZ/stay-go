package com.lfey.statygo.contoroller;

import com.lfey.statygo.service.PhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos/")
@Tag(name = "Photo API", description = "Api for management photo")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhoto(@RequestBody List<Long> ids) {
        photoService.deletePhoto(ids);
    }
}
