package com.project.studyenglish.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {
    private Long id;
    private Long idProduct;
    private Double price;
    private Integer numberOfProducts;
    private Double totalMoney;
}
