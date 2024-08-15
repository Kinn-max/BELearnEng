package com.project.studyenglish.repository.custom;

import com.project.studyenglish.models.CategoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<CategoryEntity> getAllOptionsCategory(String categoryCode) {
        String sql = "SELECT * FROM category WHERE code_name = ? ; ";
        Query query = entityManager.createNativeQuery(sql.toString(), CategoryEntity.class);
        query.setParameter(1, categoryCode);
        return query.getResultList();
    }

}
