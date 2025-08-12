package com.project.studyenglish.dto.sound;

import java.util.List;

public record Base64AudioAnalysisRequest(
        List<Base64Audio> base64AudioList,
        String prompt
) {}