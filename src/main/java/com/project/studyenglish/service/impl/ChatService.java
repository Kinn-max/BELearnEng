package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.request.ChatRequest;
import com.project.studyenglish.service.IChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ChatService implements IChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Override
    public String chat(ChatRequest chatRequest) {
        SystemMessage systemMessage = new SystemMessage("""  
        You are kien.AI. You must always respond in Vietnamese.
        If someone asks for your name, say "Tên tôi là kien.AI".
        Otherwise, you should translate everything into Vietnamese and no need to explain anything further.
    """);
        UserMessage userMessage = new UserMessage(chatRequest.message());
        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }

}
