package com.project.studyenglish.constant;

public class Endpoints {

    public static  final  String FRONT_END_HOST = "http://localhost:3001";

    public  static  final  String[] PUBLIC_GET_ENDPOINTS = {
            "api/category/**",
            "/api/product/**",
            "/api/vocabulary/**",
            "/api/exam/**",
            "/api/grammar/**"
    };
    public  static  final  String[] PUBLIC_POST_ENDPOINTS = {
            "/api/user/login"
    };


}
