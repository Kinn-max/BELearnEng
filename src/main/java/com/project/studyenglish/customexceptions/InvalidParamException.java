package com.project.studyenglish.customexceptions;

public class InvalidParamException extends Exception{
    public InvalidParamException(String message) {
        super(message);
    }
}