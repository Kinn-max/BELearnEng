package com.project.studyenglish.controller;


import com.project.studyenglish.dto.request.CategoryRequest;
import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.service.impl.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping(value = "api/detail-order")
public class DetailOrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody OrderDetailRequest orderDetailRequest) {
        orderService.addOrder(orderDetailRequest);
        return ResponseEntity.ok("");
    }
}
