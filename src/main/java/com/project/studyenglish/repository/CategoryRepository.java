package com.project.studyenglish.repository;

import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.repository.custom.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, CategoryCustomRepository {
    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.codeName = :codeName")
    int countByCodeName(@Param("codeName") String codeName);
    @Query(value = "SELECT * FROM category ORDER BY RANDOM() LIMIT :number", nativeQuery = true)
    List<CategoryEntity> findRandomCategories(@Param("number") int number);

    // Tìm category theo level và loại
    @Query("SELECT c FROM CategoryEntity c WHERE c.level = :level AND c.codeName = :categoryType AND c.status = true ORDER BY c.sortOrder")
    List<CategoryEntity> findByLevelAndCategoryType(@Param("level") Integer level, @Param("categoryType") String categoryType);

    // Tìm category exam theo level (chỉ lấy 1 category đầu tiên)
    @Query("SELECT c FROM CategoryEntity c WHERE c.level = :level AND c.codeName = 'EXAM' AND c.status = true ORDER BY c.sortOrder LIMIT 1")
    Optional<CategoryEntity> findExamCategoryByLevel(@Param("level") Integer level);

    // Lấy tất cả exam categories sắp xếp theo level
    @Query("SELECT c FROM CategoryEntity c WHERE c.codeName = 'EXAM' AND c.status = true ORDER BY c.level, c.sortOrder")
    List<CategoryEntity> getAllExamCategoriesOrderByLevel();

    // Lấy max level của loại category
    @Query("SELECT COALESCE(MAX(c.level), 0) FROM CategoryEntity c WHERE c.codeName = :categoryType AND c.status = true")
    Integer getMaxLevel(@Param("categoryType") String categoryType);

    // Tìm categories theo codeName (compatibility method)
    @Query("SELECT c FROM CategoryEntity c WHERE c.codeName = :codeName AND c.status = true ORDER BY c.level, c.sortOrder")
    List<CategoryEntity> getAllOptionsCategoryAndStatus(@Param("codeName") String codeName);

    // Kiểm tra level có tồn tại không
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CategoryEntity c " +
            "WHERE c.level = :level AND c.codeName = :categoryType AND c.status = true")
    boolean existsByLevelAndCategoryType(@Param("level") Integer level, @Param("categoryType") String categoryType);
}

