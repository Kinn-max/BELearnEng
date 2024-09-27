package com.project.studyenglish.converter;

import com.project.studyenglish.dto.request.OrderRequest;
import com.project.studyenglish.models.OrderEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {
    @Autowired
    private ModelMapper modelMapper;
    public OrderEntity toOrderEntity(OrderRequest orderRequest) {
        OrderEntity orderEntity = modelMapper.map(orderRequest, OrderEntity.class);
        return orderEntity;
    }
}
