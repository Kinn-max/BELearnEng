package com.project.studyenglish.controller;


import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.request.OrderDetailRequest;
import com.project.studyenglish.dto.response.OrderDetailResponse;
import com.project.studyenglish.service.impl.OrderDetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Transactional
@RequestMapping(value = "api/add-cart")
public class DetailOrderController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> addCartDetailOrder( @RequestBody OrderDetailRequest orderDetailRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtTokenUtil.extractUserId(token);
        if(userId == null){
            return ResponseEntity.badRequest().body("You need to log in first!");
        }
        orderDetailService.addOrderDetail(orderDetailRequest,userId);
        return ResponseEntity.ok("Add cart successful");
    }
    @GetMapping("")
    public ResponseEntity<?> getAllItem( HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtTokenUtil.extractUserId(token);
        if(userId == null){
            return ResponseEntity.badRequest().body("You need to log in first!");
        }
        List<OrderDetailResponse> detailResponseList = orderDetailService.getAllItemInCart(userId);
        Map<String, List> response = new HashMap<>();
        response.put("data", detailResponseList);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem( @PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtTokenUtil.extractUserId(token);
        if(userId == null){
            return ResponseEntity.badRequest().body("You need to log in first!");
        }
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("Delete item successful");
    }
}
