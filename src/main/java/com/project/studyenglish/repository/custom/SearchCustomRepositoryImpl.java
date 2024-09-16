package com.project.studyenglish.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchCustomRepositoryImpl implements SearchCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> searchByName(String name) {
        String sql = "SELECT * FROM (" +
                "    SELECT 'Ngữ pháp' AS source, 'GRAMMAR' AS type, id, name, image, NULL AS code_name FROM grammar WHERE name LIKE ?1" +
                "    UNION ALL" +
                "    SELECT 'Chủ đề'   AS source, 'TMP'     AS type, id, name, image, code_name FROM category WHERE name LIKE ?1" +
                "    UNION ALL" +
                "    SELECT 'Sản phẩm' AS source, 'PRODUCT' AS type, id, name, image, NULL AS code_name FROM product WHERE name LIKE ?1" +
                ") AS combined_results " +
                "LIMIT 5";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, "%" + name + "%");
        return query.getResultList();
    }
}

