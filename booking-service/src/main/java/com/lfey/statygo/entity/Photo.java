package com.lfey.statygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "photos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    private Long fileSize;

    private String mimeType;

    @Builder.Default
    private Boolean isMain = false;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime uploadDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude
    @JsonBackReference
    private Hotel hotel;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Photo photo)) return false;
        return Objects.equals(id, photo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
