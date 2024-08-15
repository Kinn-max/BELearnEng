package com.project.studyenglish.constant;

public class Endpoints {

    public static  final  String FRONT_END_HOST = "http://localhost:3001";

    public  static  final  String[] PUBLIC_GET_ENDPOINTS = {
            "/api/vocabulary",
            "/api/**",
    };

    public  static  final  String[] PUBLIC_POST_ENDPOINTS = {
            "/api/**"
    };

    public  static  final  String[] ADMIN_GET_ENDPOINTS = {
            "/customers",
            "/customers/**"
    };
    public  static  final  String[] PUBLIC_DELETE_ENDPOINTS = {
            "/api/**",
    };
}
