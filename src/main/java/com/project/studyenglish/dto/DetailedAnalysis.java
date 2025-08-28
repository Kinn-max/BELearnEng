package com.project.studyenglish.dto;

import java.util.List;

public record DetailedAnalysis(
        int pronunciationScore,
        int fluencyScore,
        int intonationScore,
        List<String> strengths,
        List<String> improvements,
        String transcribedText,
        String encouragement
) {}