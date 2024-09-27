package com.project.studyenglish.converter;

import com.project.studyenglish.customexceptions.DataNotFoundException;
import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.dto.response.OrderDetailResponse;
import com.project.studyenglish.models.OrderDetailEntity;
import com.project.studyenglish.models.OrderEntity;
import com.project.studyenglish.models.ProductEntity;
import com.project.studyenglish.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;
    public OrderDetailEntity toOrderDetail(OrderDetailRequest orderDetailRequest, OrderEntity orderEntity){
        OrderDetailEntity orderDetailNew = modelMapper.map(orderDetailRequest, OrderDetailEntity.class);
        ProductEntity productEntity = productRepository.findById(orderDetailRequest.getIdProduct())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id"));
        orderDetailNew.setProductEntity(productEntity);
        orderDetailNew.setOrderEntity(orderEntity);
        return orderDetailNew;
    }
    public OrderDetailResponse toOrderDetailRequest(OrderDetailEntity orderDetailEntity){
        OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetailEntity, OrderDetailResponse.class);
        orderDetailResponse.setName(orderDetailEntity.getProductEntity().getName());
        orderDetailResponse.setImage(orderDetailEntity.getProductEntity().getImage());
        return orderDetailResponse;
    }

}
