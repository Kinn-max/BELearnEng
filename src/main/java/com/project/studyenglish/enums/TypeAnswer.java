package com.project.studyenglish.enums;

import java.util.Map;
import java.util.TreeMap;

public enum TypeAnswer {
    A("A"),
    B("B"),
    C("C"),
    D("D");
    private  final String answer;
    TypeAnswer(String answer) {
        this.answer = answer;
    }
    public static Map<String,String> type(){
        Map<String,String> options = new TreeMap<>();
        for(TypeAnswer answer : values()){
            options.put(answer.toString(),answer.answer);
        }
        return options;
    }


}

