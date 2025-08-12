package com.project.studyenglish.controller;


import com.project.studyenglish.dto.request.ChatRequest;
import com.project.studyenglish.service.impl.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;
    @PostMapping(value = "/chat", produces = "application/json; charset=UTF-8")
    String chat(@RequestBody ChatRequest chatRequest){
        return chatService.chat(chatRequest);
    }
}
