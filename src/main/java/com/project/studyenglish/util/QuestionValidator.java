package com.project.studyenglish.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionValidator {

    private static final Logger log = LoggerFactory.getLogger(QuestionValidator.class);

    @org.springframework.ai.tool.annotation.Tool(description = "Validate a generated English multiple-choice question for quality")
    public ValidationResult validateQuestion(String question, String correctAnswer, String topic) {
        List<String> issues = new ArrayList<>();
        boolean isValid = true;

        // 1. Check question length
        if (question == null || question.trim().length() < 10) {
            isValid = false;
            issues.add("Question is too short (minimum 10 characters)");
        }

        // 2. Check for placeholders (câu đục lỗ)
        boolean isFillInBlank = question != null && question.contains("___");

        if (!isFillInBlank && (question.contains("...") || question.contains("TODO"))) {
            isValid = false;
            issues.add("Question contains incomplete placeholder");
        }
        // 3. Validate correct answer format
        if (correctAnswer == null || !correctAnswer.matches("^[ABCD]$")) {
            isValid = false;
            issues.add("Correct answer must be A, B, C, or D");
        }

        // 4. Must end with question mark
        if (question != null && !question.trim().endsWith("?")) {
            log.warn("Question should end with a question mark");
        }

        // 5. Topic relevance
        if (topic != null && question != null && topic.length() > 3) {
            String topicKeyword = topic.toLowerCase().substring(0, Math.min(topic.length(), 5));
            if (!question.toLowerCase().contains(topicKeyword)) {
                log.info("Question may not be directly related to topic: {}", topic);
            }
        }

        log.info("Validation result: valid={}, issues={}", isValid, issues.size());
        return new ValidationResult(isValid, issues);
    }

    public static class ValidationResult {
        private boolean valid;
        private List<String> issues;

        public ValidationResult() {}

        public ValidationResult(boolean valid, List<String> issues) {
            this.valid = valid;
            this.issues = issues != null ? issues : new ArrayList<>();
        }

        // Getters & setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }

        public List<String> getIssues() { return issues; }
        public void setIssues(List<String> issues) { this.issues = issues; }

        @Override
        public String toString() {
            return String.format("ValidationResult{valid=%s, issues=%s}", valid, issues);
        }
    }
}
