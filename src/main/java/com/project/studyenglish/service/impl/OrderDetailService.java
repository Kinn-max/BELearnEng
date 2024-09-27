package com.project.studyenglish.service.impl;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.converter.OrderDetailConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.dto.response.OrderDetailResponse;
import com.project.studyenglish.models.OrderDetailEntity;
import com.project.studyenglish.models.OrderEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.OrderDetailRepository;
import com.project.studyenglish.repository.OrderRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailConverter orderDetailConverter;
    @Override
    public void addOrderDetail(OrderDetailRequest orderDetailRequest,Long userId) {

        UserEntity userEntity = userRepository.findById(userId).get();
        if(userEntity.getRoleEntity().getName().equals("ADMIN")) {
            throw new DataNotFoundException("You cant not add cart!");
        }
        OrderEntity orderNew = orderRepository.findByUserEntityAndActiveFalse(userEntity);
        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        if(orderNew == null) {
            orderNew = new OrderEntity();
            orderRepository.save(orderNew);
        }else{
            orderDetailList = orderDetailRepository.findByOrderEntity(orderNew);
        }
        OrderDetailEntity orderDetailEntity = orderDetailConverter.toOrderDetail(orderDetailRequest,orderNew);
        orderDetailRepository.save(orderDetailEntity);
        orderDetailList.add(orderDetailEntity);
        //assign value
        orderNew.setUserEntity(userEntity);
        orderNew.setOrderDetailEntityList(orderDetailList);
        orderNew.setActive(false);
        orderRepository.save(orderNew);
    }

    @Override
    public List<OrderDetailResponse> getAllItemInCart(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).get();
        OrderEntity orderNew = orderRepository.findByUserEntityAndActiveFalse(userEntity);
        if(orderNew == null) {
            throw new DataNotFoundException("You have not added any products to your cart.");
        }else {
            List<OrderDetailEntity> orderDetailList = orderDetailRepository.findByOrderEntity(orderNew);
            List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
            for (OrderDetailEntity orderDetailEntity : orderDetailList) {
                OrderDetailResponse orderDetailResponse = orderDetailConverter.toOrderDetailRequest(orderDetailEntity);
                orderDetailResponseList.add(orderDetailResponse);
            }
            return orderDetailResponseList;
        }
    }

    @Override
    public void deleteOrderDetail(Long orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }
}
