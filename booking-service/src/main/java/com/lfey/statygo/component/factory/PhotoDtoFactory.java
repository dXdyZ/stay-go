package com.lfey.statygo.component.factory;

import com.lfey.statygo.dto.PhotoDto;
import com.lfey.statygo.entity.Photo;

public class PhotoDtoFactory {
    //TODO Подумать как можно вынести base url
    public static PhotoDto createPhotoDto(Photo photo) {
        return PhotoDto.builder()
                .id(photo.getId())
                .url("http://localhost:8080/uploads/" + photo.getFileName())
                .isMain(photo.getIsMain())
                .build();
    }
}
