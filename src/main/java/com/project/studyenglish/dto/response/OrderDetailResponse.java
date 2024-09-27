package com.project.studyenglish.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Double price;
    private Integer numberOfProducts;
    private Double totalMoney;
    private String name;
    private String image;
}
