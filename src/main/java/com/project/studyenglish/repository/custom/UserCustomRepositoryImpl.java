package com.project.studyenglish.repository.custom;

import com.project.studyenglish.dto.response.UserResponse;
import com.project.studyenglish.models.CategoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<UserResponse> getAllUser() {
        String sql = "SELECT id, fullname, created_at,is_active, email FROM user where role_id = 2;";
        Query query = entityManager.createNativeQuery(sql.toString(), CategoryEntity.class);
        return query.getResultList();
    }
}
