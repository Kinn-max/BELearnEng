package com.project.studyenglish.service.impl;

import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.project.studyenglish.dto.request.AITranslateRequest;
import com.project.studyenglish.dto.response.AITranslateResponse;
import com.project.studyenglish.service.IAIService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class AIService implements IAIService {

    private static final Pattern ENGLISH_WORD_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    private final ChatClient chatClient;

    public AIService(ChatClient.Builder builder) throws IOException {
        this.chatClient = builder.build();

    }


    @Override
    public AITranslateResponse translate(AITranslateRequest request) {
        String content = request.getContent();


        if (!ENGLISH_WORD_PATTERN.matcher(content).matches()) {
            return new AITranslateResponse("please input english");
        }

        SystemMessage systemMessage = new SystemMessage("""
                You are a translation assistant.
                Always translate the user input from English into Vietnamese only.
                Additionally, for each word or phrase, return the result in this exact format:
                (<part of speech>) /<IPA>/: <Vietnamese meaning>
                Use short part of speech notations:
                - noun → (n)
                - verb → (v)
                - adjective → (adj)
                - adverb → (adv)
                - preposition → (prep)
                - conjunction → (conj)
                - pronoun → (pron)
                - interjection → (int)
                For example:
                (adj) /ˈbjuː.tɪ.fəl/: đẹp
                Do not return anything else.
            """);

        UserMessage userMessage = new UserMessage(content);

        ChatResponse response = chatClient.prompt()
                .messages(systemMessage, userMessage)
                .call()
                .chatResponse();

        return new AITranslateResponse(response.getResult().getOutput().getText());
    }
}
