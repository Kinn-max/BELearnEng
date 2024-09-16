package com.project.studyenglish.repository;

import com.project.studyenglish.models.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
}
