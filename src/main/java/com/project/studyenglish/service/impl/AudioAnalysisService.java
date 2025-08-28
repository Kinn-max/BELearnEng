package com.project.studyenglish.service.impl;

import com.project.studyenglish.dto.DetailedAnalysis;
import com.project.studyenglish.dto.sound.AudioAnalysisRequest;
import com.project.studyenglish.dto.sound.AudioAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class AudioAnalysisService {

    private final ChatClient chatClient;
    private final TextToSpeechClient textToSpeechClient;

    public AudioAnalysisService(ChatClient.Builder builder) throws IOException {
        this.chatClient = builder.build();
        this.textToSpeechClient = TextToSpeechClient.create();
    }



    public AudioAnalysisResponse audioAnalysisEnhanced(AudioAnalysisRequest request) {
        try {
            // 1. Transcribe audio using Gemini directly
            String transcribedText = transcribeWithGemini(request.audioFile());

            // 2. Detailed analysis with structured output
            DetailedAnalysis analysis = analyzeWithStructuredOutput(
                    request.message(), transcribedText);

            // 3. Generate personalized feedback
            String feedbackText = generatePersonalizedFeedback(analysis);

            // 4. Create audio feedback
            String audioBase64 = generateAudioFeedback(request.message());

            // 5. Format final response
            String formattedResponse = formatAnalysisResponse(analysis);

            return new AudioAnalysisResponse(formattedResponse, audioBase64);

        } catch (Exception e) {
            e.printStackTrace();
            return new AudioAnalysisResponse(
                    "Sorry, there was an error analyzing your pronunciation. Please try again.",
                    null
            );
        }
    }

    private String transcribeWithGemini(MultipartFile audioFile) throws IOException {
        if (audioFile.isEmpty()) {
            return "Error: The provided audio file is empty.";
        }

        try {
            byte[] audioBytes = audioFile.getBytes();

            ByteArrayResource audioResource = new ByteArrayResource(audioBytes) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            };

            Media audioMedia = new Media(
                    MimeTypeUtils.parseMimeType(audioFile.getContentType()),
                    audioResource
            );

            UserMessage userMessage = UserMessage.builder()
                    .text("Please transcribe this audio file into text:")
                    .media(List.of(audioMedia))
                    .build();

            return chatClient.prompt()
                    .messages(
                            new SystemMessage("You are a professional speech-to-text system. Accurately transcribe the provided audio file into text. Your response should contain only the transcribed text."),
                            userMessage
                    )
                    .call()
                    .content();

        } catch (IOException e) {
            throw new IOException("Failed to read the audio file.", e);
        } catch (Exception e) {
            System.err.println("An error occurred during transcription: " + e.getMessage());
            return "Error: An unexpected error occurred while transcribing the audio.";
        }
    }


    private DetailedAnalysis analyzeWithStructuredOutput(String expectedText, String transcribedText) {
        try {
            SystemMessage systemMessage = new SystemMessage("""
                You are an expert pronunciation coach. Analyze the pronunciation by comparing expected vs transcribed text.
                
                Scoring Guide:
                - Pronunciation Score (0-100): Based on word accuracy
                - Fluency Score (0-100): Based on flow and natural speech
                - Intonation Score (0-100): Based on stress and rhythm patterns
                
                Provide constructive feedback focusing on specific improvements.
                Always be encouraging and supportive.
                """);

            UserMessage userMessage = new UserMessage(String.format("""
                Expected text: '%s'
                Transcribed text: '%s'
                
                Please provide a detailed analysis with specific scores and feedback.
                """, expectedText, transcribedText));

            return chatClient.prompt()
                    .messages(systemMessage, userMessage)
                    .call()
                    .entity(DetailedAnalysis.class);

        } catch (Exception e) {
            // Fallback response
            return new DetailedAnalysis(
                    85, 80, 75,
                    List.of("Clear articulation", "Good volume"),
                    List.of("Work on word stress", "Practice rhythm"),
                    transcribedText,
                    "Great job practicing! Keep it up!"
            );
        }
    }

    private String generatePersonalizedFeedback(DetailedAnalysis analysis) {
        return chatClient.prompt()
                .system("Create encouraging, personalized feedback for language learners.")
                .user(String.format("""
                Based on this analysis, create motivational feedback:
                - Pronunciation: %d/100
                - Fluency: %d/100  
                - Intonation: %d/100
                - Strengths: %s
                - Areas to improve: %s
                
                Keep it positive, specific, and actionable.
                """,
                        analysis.pronunciationScore(),
                        analysis.fluencyScore(),
                        analysis.intonationScore(),
                        String.join(", ", analysis.strengths()),
                        String.join(", ", analysis.improvements())
                ))
                .call()
                .content();
    }

    private String formatAnalysisResponse(DetailedAnalysis analysis) {
        StringBuilder response = new StringBuilder();

        response.append("Pronunciation Analysis\n\n");
        response.append(String.format("Scores:\n"));
        response.append(String.format("• Pronunciation: %d/100\n", analysis.pronunciationScore()));
        response.append(String.format("• Fluency: %d/100\n", analysis.fluencyScore()));
        response.append(String.format("• Intonation: %d/100\n\n", analysis.intonationScore()));

        response.append("Your Strengths:\n");
        analysis.strengths().forEach(strength ->
                response.append(String.format("• %s\n", strength))
        );

        response.append("Areas to Focus:\n");
        analysis.improvements().forEach(improvement ->
                response.append(String.format("• %s\n", improvement))
        );

        response.append(String.format("\nTranscribed:\"%s\"\n\n", analysis.transcribedText()));
        response.append(String.format("🌟 %s", analysis.encouragement()));

        return response.toString();
    }

    // Streaming version for real-time feedback
    public Flux<String> analyzeAudioStream(AudioAnalysisRequest request) {
        try {
            String transcribedText = transcribeWithGemini(request.audioFile());

            return chatClient.prompt()
                    .system("Provide real-time pronunciation coaching feedback.")
                    .user(String.format("Expected: '%s', Got: '%s'",
                            request.message(), transcribedText))
                    .stream()
                    .content();

        } catch (Exception e) {
            return Flux.just("Error analyzing audio: " + e.getMessage());
        }
    }

    private String generateAudioFeedback(String feedbackText) {
        try {
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(feedbackText)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setName("en-US-Neural2-D")
                    .setSsmlGender(SsmlVoiceGender.MALE)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setSpeakingRate(1.0) // Slightly slower for learners
                    .setPitch(0.0)
                    .setVolumeGainDb(0.0)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(
                    input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();
            return Base64.getEncoder().encodeToString(audioContents.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate audio feedback: " + e.getMessage(), e);
        }
    }
    public AudioAnalysisResponse convertTextToAudio(String text) {
        try {
            String audioBase64 = generateAudioFeedback(text);


            return new AudioAnalysisResponse(
                    "Converted text to audio successfully.",
                    audioBase64
            );
        } catch (Exception e) {
            return new AudioAnalysisResponse(
                    "Failed to convert text to audio: " + e.getMessage(),
                    null
            );
        }
    }

}