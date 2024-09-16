package com.project.studyenglish.repository.custom;

import com.project.studyenglish.models.CategoryEntity;
import com.project.studyenglish.models.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public OrderEntity findOrderNotActive() {
        String sql = "SELECT * FROM orders WHERE active=false ;";
        Query query = entityManager.createNativeQuery(sql.toString(), CategoryEntity.class);
        return (OrderEntity) query.getSingleResult();
    }
}
