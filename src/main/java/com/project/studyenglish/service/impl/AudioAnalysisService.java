package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.sound.AudioAnalysisRequest;
import com.project.studyenglish.dto.sound.AudioAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.Base64;

@Service
public class AudioAnalysisService {

    private final ChatClient chatClient;
    private final TextToSpeechClient textToSpeechClient;


    public AudioAnalysisService(ChatClient.Builder builder) throws IOException {
        this.chatClient = builder.build();
        this.textToSpeechClient = TextToSpeechClient.create();
    }

    public AudioAnalysisResponse audioAnalysis(AudioAnalysisRequest request) {
        try {
            // 1. Tạm thời skip transcription vì Gemini chưa hỗ trợ audio transcription tốt
            // Thay vào đó, giả sử transcription từ frontend hoặc dùng Web Speech API
            String transcribedText = getTranscribedTextFromRequest(request);

            // 2. Compare with expected text using AI
            String analysisResult = analyzePronounciation(request.message(), transcribedText);

            // 3. Generate audio feedback using Google Cloud TTS
            String audioBase64 = generateAudioFeedback(request.message());

            return new AudioAnalysisResponse(analysisResult, audioBase64);

        } catch (Exception e) {
            e.printStackTrace(); // Log để debug
            return new AudioAnalysisResponse(
                    "Sorry, there was an error analyzing your pronunciation. Please try again. Error: " + e.getMessage(),
                    null
            );
        }
    }

    private String getTranscribedTextFromRequest(AudioAnalysisRequest request) {
        // Tạm thời return một mock transcription để test
        // Bạn có thể tích hợp với Google Speech-to-Text API sau
        return "What your name?";
    }

    private String analyzePronounciation(String expectedText, String transcribedText) {
        try {
            SystemMessage systemMessage = new SystemMessage("""
                    You are an AI pronunciation coach. Compare the expected text with the transcribed speech.
                    
                    Instructions:
                    1. Analyze pronunciation accuracy by comparing expected vs transcribed text
                    2. Identify mispronounced words (words that differ significantly)
                    3. Provide encouraging feedback
                    4. Consider that transcription may have minor errors, focus on major differences
                    
                    Format your response as:
                    - If perfect match (>95% similarity): "Excellent! Perfect pronunciation! 🎉"
                    - If good (80-95% similarity): "Great job! Minor improvements for: 'word1', 'word2'"
                    - If needs work (<80% similarity): "Keep practicing! Focus on: 'word1', 'word2', 'word3'"
                    
                    Always be encouraging and constructive. Provide specific tips if needed.
                    """);

            UserMessage userMessage = new UserMessage(String.format(
                    "Expected text: '%s'\nTranscribed text: '%s'\n\nPlease analyze the pronunciation accuracy.",
                    expectedText, transcribedText
            ));

            ChatResponse response = chatClient.prompt()
                    .messages(systemMessage, userMessage)
                    .call()
                    .chatResponse();

            return response.getResult().getOutput().getText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Great job practicing! Keep working on your pronunciation.";
        }
    }

    private String generateAudioFeedback(String feedbackText) {
        try {
            // Build the synthesis input
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(feedbackText)
                    .build();

            // Build the voice request - using Google Cloud's natural voice
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setName("en-US-Neural2-D")
                    .setSsmlGender(SsmlVoiceGender.MALE)
                    .build();


            // Select the type of audio file
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setSpeakingRate(1.1)
                    .setPitch(0.0)
                    .setVolumeGainDb(0.0)
                    .build();

            // Perform the text-to-speech request
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(
                    input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            // Convert to base64 for easy transport
            return Base64.getEncoder().encodeToString(audioContents.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate audio feedback: " + e.getMessage(), e);
        }
    }
}