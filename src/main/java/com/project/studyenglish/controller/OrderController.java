package com.project.studyenglish.controller;


import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.dto.request.OrderRequest;
import com.project.studyenglish.dto.response.OrderResponse;
import com.project.studyenglish.dto.response.UserResponse;
import com.project.studyenglish.service.impl.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Transactional
@RequestMapping(value = "api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @PostMapping("")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtTokenUtil.extractUserId(token);
        if(userId == null){
            return ResponseEntity.badRequest().body("You need to log in first!");
        }
        orderService.addOrder(orderRequest);
        return ResponseEntity.ok("Order successful");
    }
    @GetMapping("")
    public ResponseEntity<?> getAllOrderOfWaiting( ) {
        try {
            List<OrderRequest> result = orderService.getAllOrderOfWaiting();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> setStatusOrder(@PathVariable Long id,@RequestParam String name ) {
        try {
            orderService.setStatusOfOrder(name,id);
            return ResponseEntity.ok("");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/admin")
    public ResponseEntity<?> getAllOrder(HttpServletRequest request ) {
        try {
            List<OrderRequest> result = orderService.getAllOrder();
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/by-user")
    public ResponseEntity<?> getAllOrdered( HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtTokenUtil.extractUserId(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("You need to log in first!");
            }
            List<OrderResponse> result = orderService.getAllOrderedByUserId(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
