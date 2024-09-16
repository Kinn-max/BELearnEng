package com.project.studyenglish.service.impl;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.models.OrderDetailEntity;
import com.project.studyenglish.models.OrderEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.OrderDetailRepository;
import com.project.studyenglish.repository.OrderRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.IOrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService implements IOrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public OrderDetailEntity addOrderDetail(OrderDetailRequest orderDetailRequest) throws Exception {
        OrderEntity orderEntity = orderRepository.findOrderNotActive();
        if(orderEntity == null) {
            orderEntity = new OrderEntity();
            orderRepository.save(orderEntity);
        }
        OrderDetailEntity orderDetailEntity = modelMapper.map(orderDetailRequest, OrderDetailEntity.class);
        orderDetailRepository.save(orderDetailEntity);
        //get information user
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        String email = jwtUtil.extractEmail(token);
        UserEntity userEntity = userRepository.findByEmail(email).get();
        //assign value
    return null;
    }
}
