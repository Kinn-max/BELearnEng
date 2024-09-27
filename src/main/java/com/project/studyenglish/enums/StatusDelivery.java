package com.project.studyenglish.enums;

import java.util.Map;
import java.util.TreeMap;

public enum StatusDelivery {
    WAITING("Chờ xác nhận"),
    DELIVERING("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELED("Đã hủy");
    private  final String method;
    StatusDelivery(String method) {
        this.method = method;
    }
    public static Map<String,String> type(){
        Map<String,String> options = new TreeMap<>();
        for(StatusDelivery result : values()){
            options.put(result.toString(),result.method);
        }
        return options;
    }

}
