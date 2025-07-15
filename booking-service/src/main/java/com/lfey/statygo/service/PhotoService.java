package com.lfey.statygo.service;

import com.lfey.statygo.entity.Hotel;
import com.lfey.statygo.entity.Photo;
import com.lfey.statygo.exception.ValidateFileException;
import com.lfey.statygo.repository.jpaRepository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final FileStorageService fileStorageService;

    public PhotoService(PhotoRepository photoRepository, FileStorageService fileStorageService) {
        this.photoRepository = photoRepository;
        this.fileStorageService = fileStorageService;
    }


    public void uploadPhoto(List<MultipartFile> files, Hotel hotel, Integer mainPhotoIndex) {
        for (int i = 0; i < files.size(); i++) {
            validFile(files.get(i));

            boolean isMain = false;

            if (mainPhotoIndex != null) {
                isMain = (mainPhotoIndex == i);
                if (!hotel.getPhotos().isEmpty()) {
                    Photo photo = hotel.getPhotos().stream().filter(Photo::getIsMain).findFirst().get();
                    photo.setIsMain(false);
                }
            }

            photoRepository.save(Photo.builder()
                    .fileName(fileStorageService.store(files.get(i)))
                    .hotel(hotel)
                    .fileSize(files.get(i).getSize())
                    .isMain(isMain)
                    .mimeType(files.get(i).getContentType())
                    .build());
        }
    }

    public void deletePhoto(List<Long> ids) {
        photoRepository.deleteAllById(ids);
    }


    private void validFile(MultipartFile file) {
        if (file.isEmpty()) throw new ValidateFileException("File is empty");
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            throw new ValidateFileException("Only image are allowed");
    }
}
