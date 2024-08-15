package com.project.studyenglish.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now().format(FORMATTER);
        updatedAt = LocalDateTime.now().format(FORMATTER);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now().format(FORMATTER);
    }
}
