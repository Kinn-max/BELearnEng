package com.project.studyenglish.service;

import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.models.OrderDetailEntity;

public interface IOrderDetailService {
    OrderDetailEntity addOrderDetail(OrderDetailRequest order) throws Exception;
}
