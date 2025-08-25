package com.project.studyenglish.service;

import com.project.studyenglish.dto.VocabularyDto;
import com.project.studyenglish.dto.request.AITranslateRequest;
import com.project.studyenglish.dto.response.AITranslateResponse;

import java.util.List;

public interface IAIService {
    AITranslateResponse translate(AITranslateRequest word);
}
