package com.project.studyenglish.service;

import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.dto.response.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    void addOrderDetail(OrderDetailRequest order,Long userId);
    List<OrderDetailResponse> getAllItemInCart(Long userId);
    void deleteOrderDetail(Long orderDetailId);
}
