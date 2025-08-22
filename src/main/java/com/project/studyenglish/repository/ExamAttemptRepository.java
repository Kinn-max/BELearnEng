package com.project.studyenglish.repository;

import com.project.studyenglish.models.ExamAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttemptEntity, Long> {

    // Tìm attempts theo user và category
    List<ExamAttemptEntity> findByUserEntityIdAndCategoryEntityId(Long userId, Long categoryId);

    // Tìm attempts theo user và level (thông qua category.level)
    @Query("SELECT ea FROM ExamAttemptEntity ea WHERE ea.userEntity.id = :userId AND ea.categoryEntity.level = :level")
    List<ExamAttemptEntity> findByUserIdAndLevel(@Param("userId") Long userId, @Param("level") Integer level);

    // Kiểm tra đã pass level chưa
    @Query("SELECT CASE WHEN COUNT(ea) > 0 THEN true ELSE false END FROM ExamAttemptEntity ea " +
            "WHERE ea.userEntity.id = :userId AND ea.categoryEntity.level = :level AND ea.isPassed = true")
    boolean existsByUserIdAndLevelAndPassed(@Param("userId") Long userId, @Param("level") Integer level);

    // Lấy attempts theo user sắp xếp theo thời gian
    @Query("SELECT ea FROM ExamAttemptEntity ea WHERE ea.userEntity.id = :userId ORDER BY ea.attemptDate DESC")
    List<ExamAttemptEntity> findByUserIdOrderByAttemptDateDesc(@Param("userId") Long userId);

    // Lấy best score của user cho 1 level
    @Query("SELECT MAX(ea.scorePercentage) FROM ExamAttemptEntity ea " +
            "WHERE ea.userEntity.id = :userId AND ea.categoryEntity.level = :level")
    Double getBestScoreByUserAndLevel(@Param("userId") Long userId, @Param("level") Integer level);

    // Đếm số lần attempt của user cho 1 level
    @Query("SELECT COUNT(ea) FROM ExamAttemptEntity ea " +
            "WHERE ea.userEntity.id = :userId AND ea.categoryEntity.level = :level")
    Long countAttemptsByUserAndLevel(@Param("userId") Long userId, @Param("level") Integer level);
}