package com.project.studyenglish.service;

import com.project.studyenglish.dto.request.SpeakAttemptRequest;
import com.project.studyenglish.dto.response.CategoryOfSpeak;
import com.project.studyenglish.dto.response.SpeakDataResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ISpeakService {
    List<CategoryOfSpeak> getAllSpeakCategory(HttpServletRequest request);
    List<SpeakDataResponse> getAllSpeakDataByCateId(Long id);
    void submitAttempt(SpeakAttemptRequest speakAttemptRequest);
}
