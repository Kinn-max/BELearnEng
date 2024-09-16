package com.project.studyenglish.service;

import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.models.OrderEntity;

public interface IOrderService {
    OrderEntity addOrder(OrderDetailRequest order);
}
