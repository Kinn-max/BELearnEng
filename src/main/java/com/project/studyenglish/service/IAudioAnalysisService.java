package com.project.studyenglish.service;

import com.project.studyenglish.dto.sound.AudioAnalysisRequest;
import com.project.studyenglish.dto.sound.AudioAnalysisResponse;

public interface IAudioAnalysisService {
    AudioAnalysisResponse AudioAnalysis(AudioAnalysisRequest request);
}
