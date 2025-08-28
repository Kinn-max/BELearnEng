package com.project.studyenglish.repository;

import com.project.studyenglish.models.SpeakEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpeakRepository extends JpaRepository<SpeakEntity, Long> {
    List<SpeakEntity> findByCategoryId(Long categoryId);
}