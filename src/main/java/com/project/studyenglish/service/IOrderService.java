package com.project.studyenglish.service;

import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.dto.request.OrderRequest;
import com.project.studyenglish.dto.response.OrderResponse;
import com.project.studyenglish.models.OrderEntity;

import java.util.List;

public interface IOrderService {
    OrderEntity addOrder(OrderRequest order);
    List<OrderRequest> getAllOrderOfWaiting();
    void setStatusOfOrder(String name, Long id);
    List<OrderRequest> getAllOrder();
    List<OrderResponse> getAllOrderedByUserId(Long id);
}
