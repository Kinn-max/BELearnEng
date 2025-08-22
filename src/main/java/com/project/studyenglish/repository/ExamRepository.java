package com.project.studyenglish.repository;

import com.project.studyenglish.models.ExamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    Page<ExamEntity> findByCategoryEntity_Id(Long id , Pageable pageable);
    List<ExamEntity> findByCategoryEntity_Id(Long id);

    // Tìm theo level (thông qua category.level)
    @Query("SELECT e FROM ExamEntity e WHERE e.categoryEntity.level = :level AND e.categoryEntity.status = true ORDER BY e.questionOrder, e.id")
    List<ExamEntity> findByLevel(@Param("level") Integer level);

    // Tìm theo category cụ thể và sắp xếp
    @Query("SELECT e FROM ExamEntity e WHERE e.categoryEntity.id = :categoryId ORDER BY e.questionOrder, e.id")
    List<ExamEntity> findByCategoryIdOrderByQuestionOrder(@Param("categoryId") Long categoryId);

    // Đếm số câu hỏi theo level
    @Query("SELECT COUNT(e) FROM ExamEntity e WHERE e.categoryEntity.level = :level AND e.categoryEntity.status = true")
    Integer countByLevel(@Param("level") Integer level);

    // Đếm số câu hỏi theo category
    @Query("SELECT COUNT(e) FROM ExamEntity e WHERE e.categoryEntity.id = :categoryId")
    Integer countByCategoryId(@Param("categoryId") Long categoryId);

    // Lấy tổng số level (từ category) - compatibility method
    @Query("SELECT COALESCE(MAX(c.level), 0) FROM CategoryEntity c WHERE c.codeName = :categoryType AND c.status = true")
    Integer getTotalLevels(@Param("categoryType") String categoryType);

    // Random câu hỏi theo level
    @Query(value = "SELECT * FROM exam e JOIN category c ON e.category_id = c.id " +
            "WHERE c.level = :level AND c.status = true ORDER BY RAND() LIMIT :limit",
            nativeQuery = true)
    List<ExamEntity> findRandomQuestionsByLevel(@Param("level") Integer level, @Param("limit") Integer limit);
}
