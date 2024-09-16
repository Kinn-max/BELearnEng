package com.project.studyenglish.repository.custom;

import com.project.studyenglish.models.OrderEntity;

public interface OrderCustomRepository {
    OrderEntity findOrderNotActive();
}
