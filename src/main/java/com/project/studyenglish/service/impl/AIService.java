package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.request.AIRenderQuestion;
import com.project.studyenglish.dto.request.AITranslateRequest;
import com.project.studyenglish.dto.response.AIRenderQuestionResponse;
import com.project.studyenglish.dto.response.AITranslateResponse;
import com.project.studyenglish.service.IAIService;
import com.project.studyenglish.util.QuestionValidator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Service
public class AIService implements IAIService {

    private static final Pattern ENGLISH_WORD_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    private final ChatClient chatClient;
    private final QuestionValidator questionValidator;

    public AIService(ChatClient.Builder builder, QuestionValidator questionValidator) {
        this.questionValidator = questionValidator;
        this.chatClient = builder
                .build();
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

    @Override
    public List<AIRenderQuestionResponse> getRenderQuestion(AIRenderQuestion rq) {
        try {
            SystemMessage systemMessage = new SystemMessage("""
                You are an expert English learning assistant.
                For each generated question, call `validateQuestion`
                to check if it is valid before returning.
                """);

            UserMessage userMessage = new UserMessage("""
                Generate exactly %d multiple-choice questions on: %s.
                Return them as a JSON array:
                [
                  {
                    "question": "...",
                    "answerA": "...",
                    "answerB": "...",
                    "answerC": "...",
                    "answerD": "...",
                    "correctAnswer": "A/B/C/D",
                    "explanation": "..."
                  }
                ]
                """.formatted(rq.getNumber(), rq.getContent()));

            List<AIRenderQuestionResponse> questions = chatClient.prompt()
                    .messages(systemMessage, userMessage)
                    .tools(questionValidator)
                    .call()
                    .entity(new ParameterizedTypeReference<>() {});

            IntStream.range(0, questions.size())
                    .forEach(i -> questions.get(i).setStt(i + 1));
            return questions != null ? questions : Collections.emptyList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}