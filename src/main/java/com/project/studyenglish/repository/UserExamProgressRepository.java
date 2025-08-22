package com.project.studyenglish.repository;

import com.project.studyenglish.models.UserExamProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserExamProgressRepository extends JpaRepository<UserExamProgressEntity, Long> {

    Optional<UserExamProgressEntity> findByUserEntityId(Long userId);
    boolean existsByUserEntityId(Long userId);
    void deleteByUserEntityId(Long userId);
}