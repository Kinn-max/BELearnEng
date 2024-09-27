package com.project.studyenglish.repository;

import com.project.studyenglish.models.OrderEntity;
import com.project.studyenglish.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByUserEntityAndActiveFalse(UserEntity userEntity);
    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status AND o.active = true ")
    List<OrderEntity> findByWaiting(@Param("status") String status);
    List<OrderEntity> findByActiveTrue();
    List<OrderEntity> findByUserEntityAndActiveTrue(UserEntity userEntity);

}
