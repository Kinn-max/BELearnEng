package com.project.studyenglish.repository;

import com.project.studyenglish.models.OrderEntity;
import com.project.studyenglish.repository.custom.OrderCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, OrderCustomRepository {
}
