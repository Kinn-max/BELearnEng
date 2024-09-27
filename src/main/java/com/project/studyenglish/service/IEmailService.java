package com.project.studyenglish.service;

public interface IEmailService {
    public void sendMessage(String from, String to, String subject, String text);
}
