package com.lfey.statygo.component.factory;

import com.lfey.statygo.dto.PhotoDto;
import com.lfey.statygo.entity.Photo;

public class PhotoDtoFactory {
    private static String mainUrl;

    public static void setMainUrl(String mainUrl) {
        PhotoDtoFactory.mainUrl = mainUrl;
    }

    public static PhotoDto createPhotoDto(Photo photo) {
        return PhotoDto.builder()
                .id(photo.getId())
                .url(mainUrl +  photo.getFileName())
                .isMain(photo.getIsMain())
                .build();
    }
}
