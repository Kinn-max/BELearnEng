package com.project.studyenglish.service.impl;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.models.OrderDetailEntity;
import com.project.studyenglish.models.OrderEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.OrderDetailRepository;
import com.project.studyenglish.repository.OrderRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.IOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {

    @Override
    public OrderEntity addOrder(OrderDetailRequest orderDetailRequest) {


        return null;
    }
}
