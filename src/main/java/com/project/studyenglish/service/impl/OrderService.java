package com.project.studyenglish.service.impl;
;
import com.project.studyenglish.converter.OrderConverter;
import com.project.studyenglish.converter.OrderDetailConverter;
import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.dto.request.OrderRequest;
import com.project.studyenglish.dto.response.OrderDetailResponse;
import com.project.studyenglish.dto.response.OrderResponse;
import com.project.studyenglish.enums.StatusDelivery;
import com.project.studyenglish.models.OrderDetailEntity;
import com.project.studyenglish.models.OrderEntity;
import com.project.studyenglish.models.UserEntity;
import com.project.studyenglish.repository.OrderRepository;
import com.project.studyenglish.repository.UserRepository;
import com.project.studyenglish.service.IOrderService;

import com.project.studyenglish.util.Email;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Email email;
    @Autowired
    private OrderDetailConverter orderDetailConverter;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Override
    public OrderEntity addOrder(OrderRequest orderRequest) {
        UserEntity userEntity = userRepository.findById(orderRequest.getUserId())
                     .orElseThrow(() -> new DataNotFoundException("User not found"));
        OrderEntity addOrder = orderRepository.findByUserEntityAndActiveFalse(userEntity);
        addOrder.setTotalMoney(orderRequest.getTotalMoney());
        addOrder.setOrderDate(new Date());
        addOrder.setEmail(orderRequest.getEmail());
        addOrder.setPhoneNumber(orderRequest.getPhoneNumber());
        addOrder.setFullName(orderRequest.getFullName());
        addOrder.setNote(orderRequest.getNote());
        addOrder.setPaymentMethod(orderRequest.getPaymentMethod());
        addOrder.setShippingAddress(orderRequest.getShippingAddress());
        addOrder.setShippingMethod(orderRequest.getShippingMethod());
        addOrder.setStatus("WAITING");
        addOrder.setActive(true);
        orderRepository.save(addOrder);
        String detail = "";
        double total = addOrder.getTotalMoney();;
        List<OrderDetailEntity> orderDetailEntityList = addOrder.getOrderDetailEntityList();
        for (OrderDetailEntity orderDetailEntity : orderDetailEntityList) {
            detail += "<p>- Sản phẩm: "+orderDetailEntity.getProductEntity().getName()+"</p>\n";
            detail += "<p>- Số lượng:"+orderDetailEntity.getNumberOfProducts()+"</p>\n";
        }
        detail += "<p><strong><span style=\"color: #ff0000;\">Tổng giá:</span></strong>"+total+"</p>\n";
        email.sendEmailDetailOrder(addOrder.getEmail(), detail);
        return null;
    }

    @Override
    public List<OrderRequest> getAllOrderOfWaiting() {
        List<OrderEntity> orderEntityList = orderRepository.findByWaiting("WAITING");
        List<OrderRequest> orderRequestList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList) {
            OrderRequest orderRequest = modelMapper.map(orderEntity, OrderRequest.class);
            Map<String,String> type = StatusDelivery.type();
            String displayValue = type.get(orderEntity.getStatus());
            orderRequest.setStatus(displayValue);
            orderRequestList.add(orderRequest);
        }
        return orderRequestList;
    }

    @Override
    public void setStatusOfOrder(String name, Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).get();
        orderEntity.setStatus(name);

    }

    @Override
    public List<OrderRequest> getAllOrder() {
        List<OrderEntity> orderEntityList = orderRepository.findByActiveTrue();
        List<OrderRequest> orderRequestList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList) {
            OrderRequest orderRequest = modelMapper.map(orderEntity, OrderRequest.class);
            Map<String,String> type = StatusDelivery.type();
            String displayValue = type.get(orderEntity.getStatus());
            orderRequest.setStatus(displayValue);
            orderRequestList.add(orderRequest);
        }
        return orderRequestList;
    }

    @Override
    public List<OrderResponse> getAllOrderedByUserId(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        List<OrderEntity> orderEntityList = orderRepository.findByUserEntityAndActiveTrue(userEntity);
        List<OrderResponse> orderRequestList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList) {
            OrderResponse orderResponse = modelMapper.map(orderEntity, OrderResponse.class);
            Map<String,String> type = StatusDelivery.type();
            String displayValue = type.get(orderEntity.getStatus());
            orderResponse.setStatus(displayValue);
            //convert to orderdetailresponse
            List<OrderDetailEntity> list = orderEntity.getOrderDetailEntityList();
            List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
            for (OrderDetailEntity orderDetailEntity : list) {
                OrderDetailResponse orderDetailResponse = orderDetailConverter.toOrderDetailRequest(orderDetailEntity);
                orderDetailResponseList.add(orderDetailResponse);
            }
            orderResponse.setOrderDetailEntityList(orderDetailResponseList);
            orderRequestList.add(orderResponse);
        }
        return orderRequestList;
    }
}
